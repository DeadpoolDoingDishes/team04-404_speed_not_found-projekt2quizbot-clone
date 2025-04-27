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

    }

    private void validateConfiguration() {

    }

    public String getApiKey() {

        return "";
    }

    public int getServerPort() {

        return 0;
    }

    public String getServerHost() {

        return "";
    }

    public String getCorsOrigins() {

        return "";
    }

    public String getCorsMethods() {

        return "";
    }

    public String getCorsHeaders() {

        return "";
    }
}