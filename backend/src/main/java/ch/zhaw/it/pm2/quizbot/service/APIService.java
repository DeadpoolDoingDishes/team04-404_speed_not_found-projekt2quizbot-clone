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

    @Value("${aiml.api.key}")
    private String apiKey;

    private final OkHttpClient httpClient;

    public APIService() {
        this.httpClient = new OkHttpClient();
    }

    public List<Flashcard> generateFlashcards(String topic, String language, int count) {
        List<Flashcard> flashcards = new ArrayList<>();
        try {
            String systemContent = "You are an AI assistant who creates educational flashcards. " +
                    "Always respond with a valid JSON array containing flashcard objects with 'question' and 'answer' fields.";

            String userContent = String.format(
                    "Create %d flashcards about the topic '%s'. " +
                            "Respond in %s language, but keep technical terms like '%s' unchanged. " +
                            "Format your response as a JSON array with objects containing 'question' and 'answer' fields. " +
                            "Example format: [{\"question\": \"Question 1?\", \"answer\": \"Answer 1\"}, ...]",
                    count, topic, language.equals("DE") ? "German" : "English", topic
            );

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", new JSONArray()
                    .put(new JSONObject().put("role", "system").put("content", systemContent))
                    .put(new JSONObject().put("role", "user").put("content", userContent))
            );

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

                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray choices = jsonResponse.getJSONArray("choices");
                    if (choices.length() > 0) {
                        String content = choices.getJSONObject(0).getJSONObject("message").getString("content");
                        LOGGER.info("Content: " + content);

                        // Try to extract JSON array from the content
                        String jsonArrayString = extractJsonArray(content);
                        JSONArray flashcardArray = new JSONArray(jsonArrayString);

                        for (int i = 0; i < flashcardArray.length(); i++) {
                            JSONObject flashcardJson = flashcardArray.getJSONObject(i);
                            String question = flashcardJson.getString("question");
                            String answer = flashcardJson.getString("answer");
                            flashcards.add(new Flashcard(question, answer));
                        }
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "No error body";
                    LOGGER.severe("API request failed with status code: " + response.code() + ", body: " + errorBody);
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Error generating flashcards: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Unexpected error: " + e.getMessage());
        }
        return flashcards;
    }

    private String extractJsonArray(String content) {
        // Remove any markdown code block markers
        content = content.replace("```json", "").replace("```", "").trim();

        // Find the first [ and last ]
        int startIndex = content.indexOf('[');
        int endIndex = content.lastIndexOf(']') + 1;

        if (startIndex >= 0 && endIndex > startIndex) {
            return content.substring(startIndex, endIndex);
        }

        // If no array found, try to parse the entire content
        return content;
    }

    private List<Flashcard> parseFlashcardsFromJson(String jsonResponse) {
        List<Flashcard> flashcards = new ArrayList<>();

        try {
            // Try to extract JSON array from the response
            String jsonArrayString = jsonResponse;

            // If the response contains text before or after the JSON array, extract just the array
            if (!jsonResponse.trim().startsWith("[")) {
                int startIndex = jsonResponse.indexOf("[");
                int endIndex = jsonResponse.lastIndexOf("]") + 1;
                if (startIndex >= 0 && endIndex > startIndex) {
                    jsonArrayString = jsonResponse.substring(startIndex, endIndex);
                }
            }

            JSONArray jsonArray = new JSONArray(jsonArrayString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject flashcardJson = jsonArray.getJSONObject(i);
                String question = flashcardJson.getString("question");
                String answer = flashcardJson.getString("answer");
                flashcards.add(new Flashcard(question, answer));
            }
        } catch (Exception e) {
            System.err.println("Error parsing flashcards from JSON: " + e.getMessage());
        }

        return flashcards;
    }

    private String callAimlApi(String systemMessage, String userMessage) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Build request JSON
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "gpt-4o");

        JSONArray messages = new JSONArray();

        // System message
        JSONObject sysMsg = new JSONObject();
        sysMsg.put("role", "system");
        sysMsg.put("content", systemMessage);
        messages.put(sysMsg);

        // User message
        JSONObject usrMsg = new JSONObject();
        usrMsg.put("role", "user");
        usrMsg.put("content", userMessage);
        messages.put(usrMsg);

        jsonBody.put("messages", messages);

        // Create request
        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(AIML_CHAT_COMPLETIONS_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        // Execute request
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response);
            }

            String responseData = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseData);

            JSONArray choices = jsonResponse.getJSONArray("choices");
            JSONObject firstChoice = choices.getJSONObject(0);
            JSONObject messageObj = firstChoice.getJSONObject("message");
            String content = messageObj.getString("content");

            return content.trim();
        }
    }

}

