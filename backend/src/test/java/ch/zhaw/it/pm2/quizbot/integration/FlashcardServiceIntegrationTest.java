package ch.zhaw.it.pm2.quizbot.integration;

import ch.zhaw.it.pm2.quizbot.model.Flashcard;
import ch.zhaw.it.pm2.quizbot.repository.FlashcardRepository;
import ch.zhaw.it.pm2.quizbot.service.APIService;
import ch.zhaw.it.pm2.quizbot.service.FlashcardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class FlashcardServiceIntegrationTest {

    @Autowired
    private FlashcardService flashcardService;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @MockBean
    private APIService apiService;

    @BeforeEach
    void setUp() {
        flashcardRepository.deleteAll();
    }

    @Test
    void testCompleteFlashcardWorkflow() {
        // Arrange - Mock API generating flashcards
        List<Flashcard> generatedCards = List.of(
                new Flashcard("Q1", "A1"),
                new Flashcard("Q2", "A2")
        );
        when(apiService.generateFlashcards("Java", "EN", 2))
                .thenReturn(generatedCards);

        // Act 1 - Generate and save flashcards
        List<Flashcard> savedCards = flashcardService.generateAndSaveFlashcards("Java", "EN", 2);

        // Assert 1 - Verify cards were saved
        assertEquals(2, savedCards.size());
        List<Flashcard> dbCards = flashcardRepository.findAll();
        assertEquals(2, dbCards.size());

        // Act 2 - Mark one flashcard as correct
        flashcardService.markFlashcard(dbCards.get(0).getId(), true);

        // Assert 2 - Verify the card was marked and points calculated
        assertEquals(1, flashcardService.calculatePoints());

        // Act 3 - Get unpracticed cards
        List<Flashcard> unpracticedCards = flashcardService.getUnpracticedFlashcards();

        // Assert 3 - Verify only one card remains unpracticed
        assertEquals(1, unpracticedCards.size());
        assertEquals(dbCards.get(1).getId(), unpracticedCards.get(0).getId());

        // Act 4 - Reset all cards
        flashcardService.resetAllFlashcards();

        // Assert 4 - Verify all cards are now unpracticed
        assertEquals(2, flashcardService.getUnpracticedFlashcards().size());
        assertEquals(0, flashcardService.calculatePoints());
    }
}
