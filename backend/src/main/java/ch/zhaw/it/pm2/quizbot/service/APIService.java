package ch.zhaw.it.pm2.quizbot.service;

import ch.zhaw.it.pm2.quizbot.model.Flashcard;

import java.io.IOException;
import java.util.List;

@Service
public class APIService {

    @Value("${aiml.api.key}")
    private String apiKey;

    private final OkHttpClient httpClient;

    public APIService() {
        this.httpClient = new OkHttpClient();
    }

    public List<Flashcard> generateFlashcards(String topic, String language, int count) {

    }

    private String extractJsonArray(String content) {
    }

    private List<Flashcard> parseFlashcardsFromJson(String jsonResponse) {

    }

    private String callAimlApi(String systemMessage, String userMessage) throws IOException {

    }
}

