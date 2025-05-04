package ch.zhaw.it.pm2.quizbot.service;

import ch.zhaw.it.pm2.quizbot.model.Flashcard;
import ch.zhaw.it.pm2.quizbot.repository.FlashcardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing flashcards.
 */
@Service
public class FlashcardService {
    private final FlashcardRepository repository;
    private final APIService apiService;


    @Autowired
    public FlashcardService(FlashcardRepository repository, APIService apiService) {
        this.repository = repository;
        this.apiService = apiService;

    }

    /**
     * Generates flashcards using the AIML service and saves them to the repository.
     *
     * @param topic    the topic for which flashcards should be generated
     * @param language the language in which the flashcards should be generated
     * @param count    the number of flashcards to generate
     * @return a list of saved flashcards
     */
    public List<Flashcard> generateAndSaveFlashcards(String topic, String language, int count) {
        List<Flashcard> flashcards = apiService.generateFlashcards(topic, language, count);
        return repository.saveAll(flashcards);
    }

    /**
     * Retrieves all flashcards from the repository.
     *
     * @return a list of all flashcards
     */
    public List<Flashcard> getAllFlashcards() {
        return repository.findAll();
    }

    /**
     * Marks a specific flashcard as correct or incorrect based on the given flag.
     *
     * @param id        the ID of the flashcard to be updated
     * @param isCorrect true if the answer was correct, false otherwise
     */
    public void markFlashcard(String id, boolean isCorrect) {
        Optional<Flashcard> flashcardOpt = repository.findById(id);
        if (flashcardOpt.isPresent()) {
            Flashcard flashcard = flashcardOpt.get();
            flashcard.setCorrect(isCorrect);
            repository.save(flashcard);
        }
    }

    /**
     * Calculates the number of flashcards that have been marked as correct.
     *
     * @return the number of correct flashcards
     */
    public int calculatePoints() {
        return (int) repository.findAll().stream()
                .filter(Flashcard::isCorrect)
                .count();
    }

    /**
     * Resets the correctness flag of all flashcards to false.
     *
     * @return a list of all flashcards after being reset
     */
    public List<Flashcard> resetAllFlashcards() {
        List<Flashcard> allFlashcards = repository.findAll();

        // Reset the isCorrect flag for all flashcards
        allFlashcards.forEach(flashcard -> flashcard.setCorrect(false));

        // Save all the updated flashcards
        return repository.saveAll(allFlashcards);
    }
}