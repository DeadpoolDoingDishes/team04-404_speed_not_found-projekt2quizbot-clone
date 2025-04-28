package ch.zhaw.it.pm2.quizbot.controller;

import ch.zhaw.it.pm2.quizbot.model.Flashcard;
import ch.zhaw.it.pm2.quizbot.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/flashcards")
@CrossOrigin(origins = "http://localhost:3000")
public class FlashcardController {

    private final FlashcardService flashcardService;

    @Autowired
    public FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @PostMapping("/generate")
    public ResponseEntity<List<Flashcard>> generateFlashcards(@RequestBody Map<String, Object> request) {
        String topic = (String) request.get("topic");
        String language = (String) request.get("language");
        int count = ((Number) request.get("count")).intValue();

        List<Flashcard> flashcards = flashcardService.generateFlashcards(topic, language, count);
        return ResponseEntity.ok(flashcards);
    }

    @GetMapping
    public ResponseEntity<List<Flashcard>> getAllFlashcards() {
        List<Flashcard> flashcards = flashcardService.getAllFlashcards();
        return ResponseEntity.ok(flashcards);
    }

    @PostMapping("/mark")
    public ResponseEntity<Void> markFlashcard(@RequestBody Map<String, Object> request) {
        String id = (String) request.get("id");
        boolean isCorrect = (Boolean) request.get("isCorrect");
        flashcardService.markFlashcard(id, isCorrect);
        return ResponseEntity.ok().build();
    }
}
