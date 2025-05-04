package ch.zhaw.it.pm2.quizbot.edgecases;


import ch.zhaw.it.pm2.quizbot.model.Flashcard;
import ch.zhaw.it.pm2.quizbot.repository.FlashcardRepository;
import ch.zhaw.it.pm2.quizbot.service.APIService;
import ch.zhaw.it.pm2.quizbot.service.FlashcardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceEdgeCaseTests {

    @Mock
    private FlashcardRepository flashcardRepository;

    @Mock
    private APIService apiService;

    @InjectMocks
    private FlashcardService flashcardService;


    // this test ensures that if the findById method returns an empty optional, the save method is not called
    @Test
    void markFlashcard_NonExistentId() {
        // Arrange
        String nonExistentId = "non-existent-id";

        // heres the question tho if the findById method returns an empty optional or if it throws an exception? (test manually!
        when(flashcardRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        flashcardService.markFlashcard(nonExistentId, true);

        // Assert
        verify(flashcardRepository).findById(nonExistentId);
        verify(flashcardRepository, never()).save(any(Flashcard.class));
    }
}