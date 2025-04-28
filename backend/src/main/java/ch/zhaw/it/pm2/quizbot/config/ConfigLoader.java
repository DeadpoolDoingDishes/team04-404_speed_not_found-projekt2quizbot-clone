package ch.zhaw.it.pm2.quizbot.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigLoader {
    private static final Logger LOGGER = Logger.getLogger(ConfigLoader.class.getName());
    private static final String CONFIG_FILE = "config.properties";
    private static final String API_KEY_PROPERTY = "aiml.api.key";
    private static final String SERVER_PORT_PROPERTY = "server.port";
    private static final String SERVER_HOST_PROPERTY = "server.host";
    private static final String CORS_ORIGINS_PROPERTY = "cors.origins";
    private static final String CORS_METHODS_PROPERTY = "cors.methods";
    private static final String CORS_HEADERS_PROPERTY = "cors.headers";

    private final Properties properties;

    public ConfigLoader() {
        this.properties = new Properties();
        loadConfiguration();
    }

    private void loadConfiguration() {
        try {
            // Try to load from config file first
            try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                properties.load(fis);
                LOGGER.info("Configuration loaded from " + CONFIG_FILE);
            } catch (IOException e) {
                LOGGER.warning("Could not load config file: " + e.getMessage());
            }

            // Override with environment variables if they exist
            String apiKey = System.getenv("AIML_API_KEY");
            if (apiKey != null && !apiKey.isEmpty()) {
                properties.setProperty(API_KEY_PROPERTY, apiKey);
                LOGGER.info("API key loaded from environment variable");
            }

            validateConfiguration();
        } catch (Exception e) {
            LOGGER.severe("Error loading configuration: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private void validateConfiguration() {
        if (getApiKey() == null || getApiKey().isEmpty()) {
            throw new RuntimeException("API key is required but not found in config file or environment variable");
        }
    }

    public String getApiKey() {
        return properties.getProperty(API_KEY_PROPERTY);
    }

    public int getServerPort() {
        String port = properties.getProperty(SERVER_PORT_PROPERTY, "8080");
        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid port number in config, using default 8080");
            return 8080;
        }
    }

    public String getServerHost() {
        return properties.getProperty(SERVER_HOST_PROPERTY, "localhost");
    }

    public String getCorsOrigins() {
        return properties.getProperty(CORS_ORIGINS_PROPERTY, "*");
    }

    public String getCorsMethods() {
        return properties.getProperty(CORS_METHODS_PROPERTY, "GET,POST,OPTIONS");
    }

    public String getCorsHeaders() {
        return properties.getProperty(CORS_HEADERS_PROPERTY, "Content-Type,Authorization");
    }
}