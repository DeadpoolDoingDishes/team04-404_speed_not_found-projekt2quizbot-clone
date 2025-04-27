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

    }

    @GetMapping
    public ResponseEntity<List<Flashcard>> getAllFlashcards() {
    }

    @PostMapping("/mark")
    public ResponseEntity<Void> markFlashcard(@RequestBody Map<String, Object> request) {

    }
}
