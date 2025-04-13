package ch.zhaw.it.pm2.quizbot.service;

import ch.zhaw.it.pm2.quizbot.model.Points;
import ch.zhaw.it.pm2.quizbot.repository.PointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointsService {
    private final PointsRepository repository;

    @Autowired
    public PointsService(PointsRepository repository) {
        this.repository = repository;
    }

    public Points getPoints() {
    }

    public Points addPoints(int pointsToAdd) {
    }
}