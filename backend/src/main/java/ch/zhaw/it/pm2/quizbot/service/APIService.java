package ch.zhaw.it.pm2.quizbot.service;

import ch.zhaw.it.pm2.quizbot.model.Flashcard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for communicating with the AIML API to generate flashcards.
 */
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

    /**
     * Generates a list of flashcards by sending a request to the AIML API with the given topic, language and count.
     *
     * @param topic    the subject to generate flashcards about
     * @param language the language code ("DE", "EN") to use in the generated flashcards
     * @param count    the number of flashcards to generate
     * @return a list of generated {@link Flashcard} objects
     */
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

    /**
     * Builds the JSON request body for the AIML API containing the system and user messages.
     *
     * @param topic    the topic for flashcard generation
     * @param language the language code ("DE", "EN") used in the prompt
     * @param count    the number of flashcards to generate
     * @return a JSONObject representing the API request body
     */
    private JSONObject createRequestBody(String topic, String language, int count) {
        String systemContent = "You are an AI assistant who creates educational flashcards. " + "Always respond with a valid JSON array containing flashcard objects with 'question' and 'answer' fields.";

        String userContent = String.format("Create %d flashcards about the topic '%s'. " + "Respond in %s language, but keep technical terms like '%s' unchanged. " + "Format your response as a JSON array with objects containing 'question' and 'answer' fields. " + "Example format: [{\"question\": \"Question 1?\", \"answer\": \"Answer 1\"}, ...]", count, topic, getLanguageName(language), topic);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", new JSONArray().put(new JSONObject().put("role", "system").put(CONTENT, systemContent)).put(new JSONObject().put("role", "user").put(CONTENT, userContent)));
        return requestBody;
    }

    /**
     * Converts a language code into a full language name for use in the prompt.
     *
     * @param languageCode the language code (e.g., "DE", "EN")
     * @return the corresponding full language name, defaulting to "English"
     */
    private String getLanguageName(String languageCode) {
        switch (languageCode.toUpperCase()) {
            case "DE":
                return "German";
            case "EN":
                return "English";
            case "FR":
                return "French";
            case "ES":
                return "Spanish";
            default:
                return "English"; // Fallback
        }
    }


    /**
     * Creates an HTTP POST request to the AIML API with the given JSON body.
     *
     * @param requestBody the JSON payload to send
     * @return a configured OkHttp Request object
     */
    private Request createAPIRequest(JSONObject requestBody) {
        Request request = new Request.Builder().url(AIML_CHAT_COMPLETIONS_URL).header("Content-Type", "application/json").header("Authorization", "Bearer " + apiKey).post(RequestBody.create(requestBody.toString(), MediaType.parse("application/json"))).build();
        return request;
    }

    /**
     * Sends the HTTP POST request to the AIML API and returns the response body as a string.
     *
     * @param requestBody the JSON body to send
     * @return the response body as a string if successful, otherwise null
     * @throws IOException if the HTTP request fails
     */
    private String sendRequest(JSONObject requestBody) throws IOException {
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

    /**
     * Parses the response from the AIML API and extracts a list of flashcards.
     *
     * @param responseBody the raw JSON response as a string
     * @return a list of Flashcard objects parsed from the response
     */
    private List<Flashcard> parseFlashcardsFromResponse(String responseBody) {
        List<Flashcard> flashcards = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray choices = jsonResponse.getJSONArray("choices");
            if (!choices.isEmpty()) {
                String content = choices.getJSONObject(0).getJSONObject("message").getString(CONTENT);
                String jsonArrayString = extractJsonArray(content);
                JSONArray flashcardArray = new JSONArray(jsonArrayString);
                for (int i = 0; i < flashcardArray.length(); i++) {
                    JSONObject flashcardJson = flashcardArray.getJSONObject(i);
                    String question = flashcardJson.getString("question");
                    String answer = flashcardJson.getString("answer");
                    flashcards.add(new Flashcard(question, answer));
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error parsing flashcards from response: " + e.getMessage());
        }
        return flashcards;
    }


    /**
     * Extracts the JSON array portion from a string, removing any code block formatting.
     *
     * @param content the string containing the JSON array
     * @return a string representing the raw JSON array
     */
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

