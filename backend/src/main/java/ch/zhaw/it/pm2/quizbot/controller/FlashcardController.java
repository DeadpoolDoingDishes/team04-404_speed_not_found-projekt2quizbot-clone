package ch.zhaw.it.pm2.quizbot.controller;

import ch.zhaw.it.pm2.quizbot.model.Flashcard;
import ch.zhaw.it.pm2.quizbot.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller that provides endpoints for managing flashcards,
 * including generation, retrieval, marking correctness, and resetting state.
 */
@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {
    private final FlashcardService flashcardService;

    /**
     * Constructs the FlashcardController with a FlashcardService dependency.
     *
     * @param flashcardService the service used to manage flashcards
     */
    @Autowired
    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    /**
     * Generates and saves a list of flashcards based on topic, language and count.
     *
     * @param request a map containing keys "topic", "language", and "count"
     * @return a list of generated flashcards
     */
    @PostMapping("/generate")
    public ResponseEntity<List<Flashcard>> generateAndSaveFlashcards(@RequestBody Map<String, Object> request) {
        String topic = (String) request.get("topic");
        String language = (String) request.get("language");
        int count = ((Number) request.get("count")).intValue();

        List<Flashcard> flashcards = flashcardService.generateAndSaveFlashcards(topic, language, count);
        return ResponseEntity.ok(flashcards);
    }

    /**
     * Retrieves all stored flashcards.
     *
     * @return a list of all flashcards
     */
    @GetMapping
    public ResponseEntity<List<Flashcard>> getAllFlashcards() {
        List<Flashcard> flashcards = flashcardService.getAllFlashcards();
        return ResponseEntity.ok(flashcards);
    }

    /**
     * Marks a flashcard as correct or incorrect.
     *
     * @param request a map containing keys "id" and "isCorrect"
     * @return an empty HTTP 200 OK response
     */
    @PostMapping("/mark")
    public ResponseEntity<Void> markFlashcard(@RequestBody Map<String, Object> request) {
        String id = (String) request.get("id");
        boolean isCorrect = (Boolean) request.get("isCorrect");
        flashcardService.markFlashcard(id, isCorrect);
        return ResponseEntity.ok().build();
    }

    /**
     * Resets all flashcards to their initial state.
     *
     * @return a confirmation message as HTTP response
     */
    @PostMapping("/reset")
    public ResponseEntity<String> resetAllFlashcards() {
        flashcardService.resetAllFlashcards();
        return ResponseEntity.ok("All flashcards have been reset.");
    }
}
