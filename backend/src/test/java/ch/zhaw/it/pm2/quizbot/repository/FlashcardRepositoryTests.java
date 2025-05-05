package com.quizbot.repository;

import com.quizbot.model.Flashcard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FlashcardRepositoryTests {

    @Autowired
    private FlashcardRepository flashcardRepository;



    @Test
    void testSaveAndFindFlashcard() {
        // Arrange
        Flashcard flashcard = new Flashcard("What is Spring?", "A Java framework");

        // Act
        Flashcard savedFlashcard = flashcardRepository.save(flashcard);
        Optional<Flashcard> foundFlashcard = flashcardRepository.findById(savedFlashcard.getId());

        // Assert
        assertTrue(foundFlashcard.isPresent());
        assertEquals("What is Spring?", foundFlashcard.get().getQuestion());
        assertEquals("A Java framework", foundFlashcard.get().getAnswer());
    }

    @Test
    void testFindAllFlashcards() {
        // Arrange
        flashcardRepository.save(new Flashcard("Question 1", "Answer 1"));
        flashcardRepository.save(new Flashcard("Question 2", "Answer 2"));
        flashcardRepository.save(new Flashcard("Question 3", "Answer 3"));

        // Act
        List<Flashcard> flashcards = flashcardRepository.findAll();

        // Assert
        assertEquals(3, flashcards.size());
    }
}