package ch.zhaw.it.pm2.quizbot.controller;

import ch.zhaw.it.pm2.quizbot.model.Points;
import ch.zhaw.it.pm2.quizbot.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/points")
@CrossOrigin(origins = "http://localhost:3000")
public class PointsController {
    private final PointsService pointsService;

    @Autowired
    public PointsController(PointsService pointsService) {
        this.pointsService = pointsService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Integer>> getPoints() {
        Points points = pointsService.getPoints();
        return ResponseEntity.ok(Map.of("points", points.getPoints()));
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Integer>> addPoints(@RequestBody Map<String, Integer> request) {
        int pointsToAdd = request.get("points");
        Points points = pointsService.addPoints(pointsToAdd);
        return ResponseEntity.ok(Map.of("points", points.getPoints()));
    }
}