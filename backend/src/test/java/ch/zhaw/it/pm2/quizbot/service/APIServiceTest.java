package com.quizbot.service;

import com.quizbot.model.Flashcard;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APIServiceTest {

    @Mock
    private OkHttpClient mockHttpClient;

    @Mock
    private Call mockCall;

    @Mock
    private Response mockResponse;

    @Mock
    private ResponseBody mockResponseBody;

    @InjectMocks
    private AIMLService aimlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(aimlService, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(aimlService, "httpClient", mockHttpClient);
    }
    //this tests the generateFlashcard method of our api service class with a successful response from the API
    @Test
    void generateFlashcards_SuccessfulResponse() throws IOException {
        // Arrange
        String successfulJson = "{\n" +
                "  \"choices\": [\n" +
                "    {\n" +
                "      \"message\": {\n" +
                "        \"content\": \"[{\\\"question\\\": \\\"What is Spring Boot?\\\", \\\"answer\\\": \\\"A Java framework\\\"}," +
                "{\\\"question\\\": \\\"What is JPA?\\\", \\\"answer\\\": \\\"Java Persistence API\\\"}]\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn(successfulJson);

        // Act
        List<Flashcard> result = aimlService.generateFlashcards("Spring Boot", "EN", 2);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("What is Spring Boot?", result.get(0).getQuestion());
        assertEquals("A Java framework", result.get(0).getAnswer());
        assertEquals("What is JPA?", result.get(1).getQuestion());
        assertEquals("Java Persistence API", result.get(1).getAnswer());
    }
    // this test ensures that the generateFlashcard method handles an error response from the API by leaving the flashcards list empty
    @Test
    void generateFlashcards_ApiError() throws IOException {
        // Arrange
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(false);

        // Act
        List<Flashcard> result = aimlService.generateFlashcards("Spring Boot", "EN", 2);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    // this test ensures that the generateFlashcard method handles a network error by leaving the flashcards list empty
    // would be cool if it tests the error message as well / if it loogs the error
    @Test
    void generateFlashcards_NetworkError() throws IOException {
        // Arrange
        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(new IOException("Network error"));

        // Act
        List<Flashcard> result = aimlService.generateFlashcards("Spring Boot", "EN", 2);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    void generateFlashcards_MalformedJsonResponse() throws IOException {
        // Arrange
        String malformedJson = "{ This is not valid JSON }";

        when(mockHttpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn(malformedJson);

        // Act
        List<Flashcard> result = aimlService.generateFlashcards("Spring Boot", "EN", 2);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list on malformed JSON");
    }


    // this test ensures that the extractJsonArray method correctly cleans up the json response
    @Test
    void extractJsonArray_ValidJsonArray() {
        // Arrange
        String content = "```json\n[{\"question\": \"Q1\", \"answer\": \"A1\"}, {\"question\": \"Q2\", \"answer\": \"A2\"}]\n```";

        // Act - Using reflection to test private method
        String result = ReflectionTestUtils.invokeMethod(aimlService, "extractJsonArray", content);

        // Assert
        assertEquals("[{\"question\": \"Q1\", \"answer\": \"A1\"}, {\"question\": \"Q2\", \"answer\": \"A2\"}]", result);
    }
    // this ensures that the extractJsonArray method correctly handles a json response without array markers
    @Test
    void extractJsonArray_NoArrayMarkers() {
        // Arrange
        String content = "This is some text without JSON";

        // Act - Using reflection to test private method
        String result = ReflectionTestUtils.invokeMethod(aimlService, "extractJsonArray", content);

        // Assert
        assertEquals("This is some text without JSON", result);
    }
}