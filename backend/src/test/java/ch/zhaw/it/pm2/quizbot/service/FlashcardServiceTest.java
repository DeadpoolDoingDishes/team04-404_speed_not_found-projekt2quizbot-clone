package ch.zhaw.it.pm2.quizbot.service;

import ch.zhaw.it.pm2.quizbot.model.Flashcard;
import ch.zhaw.it.pm2.quizbot.repository.FlashcardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FlashcardServiceTest {

    @Mock
    private FlashcardRepository flashcardRepository;

    @Mock
    private APIService apiService;


    @InjectMocks
    private FlashcardService flashcardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void generateAndSaveFlashcards_Success() {
        // Arrange
        String topic = "Java Programming";
        String language = "EN";
        int count = 3;

        List<Flashcard> generatedFlashcards = Arrays.asList(
                new Flashcard("What is Java?", "A programming language"),
                new Flashcard("What is a JVM?", "Java Virtual Machine"),
                new Flashcard("What is an interface?", "A reference type in Java")
        );

        when(apiService.generateFlashcards(topic, language, count))
                .thenReturn(generatedFlashcards);

        when(flashcardRepository.saveAll(anyList()))
                .thenReturn(generatedFlashcards);

        // Act
        List<Flashcard> result = flashcardService.generateAndSaveFlashcards(topic, language, count);

        verify(apiService).generateFlashcards(topic, language, count);
        verify(flashcardRepository).saveAll(generatedFlashcards);
        // Assert
        assertEquals(3, result.size());

    }
    //testing saving an empty list of flashcards
    @Test
    void generateAndSaveFlashcards_EmptyResult() {
        // Arrange
        String topic = "Obscure Topic";
        String language = "EN";
        int count = 5;

        List<Flashcard> emptyList = List.of();

        when(apiService.generateFlashcards(topic, language, count))
                .thenReturn(emptyList);

        // Act
        List<Flashcard> result = flashcardService.generateAndSaveFlashcards(topic, language, count);

        // Verify that the apiService and flashcardRepository methods were called with the correct parameters
        verify(apiService).generateFlashcards(topic, language, count);
        verify(flashcardRepository).saveAll(emptyList);

        // Assert that the result is an empty list if no flashcards were generated
        assertTrue(result.isEmpty());

    }
    // testing the getAllFlashcards method of our flashcard service class
    @Test
    void getAllFlashcards_Success() {
        // Arrange
        List<Flashcard> flashcards = Arrays.asList(
                new Flashcard("Question 1", "Answer 1"),
                new Flashcard("Question 2", "Answer 2")
        );

        when(flashcardRepository.findAll()).thenReturn(flashcards);

        // Act
        List<Flashcard> result = flashcardService.getAllFlashcards();

        verify(flashcardRepository).findAll();
        // Assert
        assertEquals(2, result.size());

    }
    // testing if the markFlashcard method of our flashcard service class works correctly

    @Test
    void markFlashcard_Correct() {
        // Arrange
        String flashcardId = "test-id";
        Flashcard flashcard = new Flashcard("Question", "Answer");
        flashcard.setId(flashcardId);

        when(flashcardRepository.findById(flashcardId))
                .thenReturn(Optional.of(flashcard));

        // Act
        flashcardService.markFlashcard(flashcardId, true);

        // Verify
        verify(flashcardRepository).findById(flashcardId);
        verify(flashcardRepository).save(flashcard);

        // Assert
        assertTrue(flashcard.isCorrect());
    }

    @Test
    void markFlashcard_Incorrect() {
        // Arrange
        String flashcardId = "test-id";
        Flashcard flashcard = new Flashcard("Question", "Answer");
        flashcard.setId(flashcardId);

        when(flashcardRepository.findById(flashcardId))
                .thenReturn(Optional.of(flashcard));

        // Act
        flashcardService.markFlashcard(flashcardId, false);

        // Assert
        verify(flashcardRepository).findById(flashcardId);
        verify(flashcardRepository).save(flashcard);
        assertFalse(flashcard.isCorrect());
    }
    //Edgecase:
    // test if the markFlashcard method handles the case when the flashcard is not found correct
    // by not calling the save method and not adding points

    @Test
    void markFlashcard_NotFound() {
        // Arrange
        String flashcardId = "non-existent-id";

        when(flashcardRepository.findById(flashcardId))
                .thenReturn(Optional.empty());

        // Act
        flashcardService.markFlashcard(flashcardId, true);

        // Assert
        verify(flashcardRepository).findById(flashcardId);
        //make sure findById returns empty
        assertTrue(flashcardRepository.findById(flashcardId).isEmpty());
        //make sure save is never called
        verify(flashcardRepository, never()).save(any(Flashcard.class));
    }
}
