package ch.zhaw.it.pm2.quizbot.repository;

import ch.zhaw.it.pm2.quizbot.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, String> {
}