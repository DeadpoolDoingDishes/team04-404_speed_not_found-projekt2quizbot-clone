package ch.zhaw.it.pm2.quizbot.service;

import ch.zhaw.it.pm2.quizbot.model.Flashcard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class APIService {
    private static final Logger LOGGER = Logger.getLogger(APIService.class.getName());
    private static final String AIML_CHAT_COMPLETIONS_URL = "https://api.aimlapi.com/v1/chat/completions";
    private static final String CONTENT = "content";


    @Value("${aiml.api.key}")
    private String apiKey;

    private final OkHttpClient httpClient;

    public APIService() {
        this.httpClient = new OkHttpClient();
    }

    public List<Flashcard> generateFlashcards(String topic, String language, int count) {
        List<Flashcard> flashcards = new ArrayList<>();
        try {
            JSONObject requestBody = createRequestBody(topic, language, count);
            String responseBody = sendRequest(requestBody);
            if (responseBody != null) {
                flashcards = parseFlashcardsFromResponse(responseBody);
            }
        } catch (IOException e) {
            LOGGER.severe("Network error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Unexpected error: " + e.getMessage());
        }
        return flashcards;
    }

    private JSONObject createRequestBody(String topic, String language, int count) {
        String systemContent = "You are an AI assistant who creates educational flashcards. " +
                "Always respond with a valid JSON array containing flashcard objects with 'question' and 'answer' fields.";

        String userContent = String.format(
                "Create %d flashcards about the topic '%s'. " +
                        "Respond in %s language, but keep technical terms like '%s' unchanged. " +
                        "Format your response as a JSON array with objects containing 'question' and 'answer' fields. " +
                        "Example format: [{\"question\": \"Question 1?\", \"answer\": \"Answer 1\"}, ...]",
                count, topic, getLanguageName(language), topic
        );

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", new JSONArray()
                .put(new JSONObject().put("role", "system").put("content", systemContent))
                .put(new JSONObject().put("role", "user").put("content", userContent))
        );
        return requestBody;
    }

    private String getLanguageName(String languageCode) {
        switch (languageCode.toUpperCase()) {
            case "DE":
                return "German";
            case "EN":
            default:
                return "English";
        }
    }



    private String sendRequest(JSONObject requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(AIML_CHAT_COMPLETIONS_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json")))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                LOGGER.info("API Response: " + responseBody);
                return responseBody;
            } else {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                LOGGER.severe("API request failed with status code: " + response.code() + ", body: " + errorBody);
            }
        }
        return null;
    }

    private List<Flashcard> parseFlashcardsFromResponse(String responseBody) {
        List<Flashcard> flashcards = new ArrayList<>();
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray choices = jsonResponse.getJSONArray("choices");
        if (choices.length() > 0) {
            String content = choices.getJSONObject(0).getJSONObject("message").getString("content");
            String jsonArrayString = extractJsonArray(content);
            JSONArray flashcardArray = new JSONArray(jsonArrayString);
            for (int i = 0; i < flashcardArray.length(); i++) {
                JSONObject flashcardJson = flashcardArray.getJSONObject(i);
                String question = flashcardJson.getString("question");
                String answer = flashcardJson.getString("answer");
                flashcards.add(new Flashcard(question, answer));
            }
        }
        return flashcards;
    }

    private String extractJsonArray(String content) {
        content = content.replace("```json", "").replace("```", "").trim();

        int startIndex = content.indexOf('[');
        int endIndex = content.lastIndexOf(']') + 1;

        if (startIndex >= 0 && endIndex > startIndex) {
            return content.substring(startIndex, endIndex);
        }

        return content;
    }

}

