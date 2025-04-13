package ch.zhaw.it.pm2.quizbot.repository;

import ch.zhaw.it.pm2.quizbot.model.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointsRepository extends JpaRepository<Points, String> {
}