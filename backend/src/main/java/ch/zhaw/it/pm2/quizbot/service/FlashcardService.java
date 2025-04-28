package ch.zhaw.it.pm2.quizbot.service;

import ch.zhaw.it.pm2.quizbot.model.Flashcard;
import ch.zhaw.it.pm2.quizbot.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FlashcardService {
    private final FlashcardRepository repository;
    private final APIService apiService;
    private final PointsService pointsService;

    @Autowired
    public FlashcardService(FlashcardRepository repository, APIService apiService, PointsService pointsService) {
        this.repository = repository;
        this.apiService = apiService;
        this.pointsService = pointsService;
    }

    public List<Flashcard> generateFlashcards(String topic, String language, int count) {
        return repository.saveAll(flashcards);
    }

    public List<Flashcard> getAllFlashcards() {
        return repository.findAll();
    }

    public void markFlashcard(String id, boolean isCorrect) {
        Optional<Flashcard> flashcardOpt = repository.findById(id);
        if (flashcardOpt.isPresent()) {
            Flashcard flashcard = flashcardOpt.get();
            flashcard.setCorrect(isCorrect);
            repository.save(flashcard);

            // Add points if the answer was correct
            if (isCorrect) {
                pointsService.addPoints(1);
            }
        }
    }
}