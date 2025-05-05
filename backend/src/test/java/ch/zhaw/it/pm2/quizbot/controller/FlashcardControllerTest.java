package com.quizbot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizbot.model.Flashcard;

import com.quizbot.service.FlashcardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FlashcardControllerTest {

    @Mock
    private FlashcardService flashcardService;

    @InjectMocks
    private FlashcardController flashcardController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(flashcardController).build();
        objectMapper = new ObjectMapper();
    }

    //testing our generateFlashcards post endpoint with a valid request
    @Test
    void generateFlashcards_ValidRequest() throws Exception {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("topic", "Spring Boot");
        requestBody.put("language", "EN");
        requestBody.put("count", 2);

        List<Flashcard> flashcards = Arrays.asList(
                new Flashcard("What is Spring Boot?", "A Java framework"),
                new Flashcard("What is an Autowired annotation?", "Dependency injection annotation")
        );

        when(flashcardService.generateAndSaveFlashcards("Spring Boot", "EN", 2)).thenReturn(flashcards);

        // Act & Assert
        mockMvc.perform(post("/api/flashcards/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].question").value("What is Spring Boot?"))
                .andExpect(jsonPath("$[0].answer").value("A Java framework"))
                .andExpect(jsonPath("$[1].question").value("What is an Autowired annotation?"))
                .andExpect(jsonPath("$[1].answer").value("Dependency injection annotation"));

        verify(flashcardService).generateAndSaveFlashcards("Spring Boot", "EN", 2);
    }

    //testing our getAllFlashcards get endpoint with a valid request
    @Test
    void getAllFlashcards_Success() throws Exception {
        // Arrange
        List<Flashcard> flashcards = Arrays.asList(
                new Flashcard("Question 1", "Answer 1"),
                new Flashcard("Question 2", "Answer 2")
        );

        when(flashcardService.getAllFlashcards()).thenReturn(flashcards);

        // Act & Assert
        mockMvc.perform(get("/api/flashcards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].question").value("Question 1"))
                .andExpect(jsonPath("$[0].answer").value("Answer 1"))
                .andExpect(jsonPath("$[1].question").value("Question 2"))
                .andExpect(jsonPath("$[1].answer").value("Answer 2"));

        verify(flashcardService).getAllFlashcards();
    }
    // testing our markFlashcard post endpoint with a valid request
    @Test
    void markFlashcard_ValidRequest() throws Exception {
        // Arrange
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "test-id");
        requestBody.put("isCorrect", true);

        // Act & Assert
        mockMvc.perform(post("/api/flashcards/mark")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk());

        verify(flashcardService).markFlashcard("test-id", true);
    }


    @Test
    void resetFlashcards_Success() throws Exception {
        // Arrange
        List<Flashcard> flashcards = Arrays.asList(
                new Flashcard("Question 1", "Answer 1"),
                new Flashcard("Question 2", "Answer 2")
        );

        when(flashcardService.resetAllFlashcards()).thenReturn(flashcards);

        // Act & Assert
        mockMvc.perform(post("/api/flashcards/reset"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));


        verify(flashcardService).resetAllFlashcards();
    }

}