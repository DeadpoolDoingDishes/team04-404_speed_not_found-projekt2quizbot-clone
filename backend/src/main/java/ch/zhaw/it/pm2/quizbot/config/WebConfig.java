package ch.zhaw.it.pm2.quizbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class for CORS (Cross-Origin Resource Sharing) settings.
 * <p>
 * This configuration automatically applies CORS headers to all API endpoints
 * based on properties defined in application.properties or application.yml.
 * Spring Boot automatically detects and applies this configuration at startup.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.origins}")
    private String corsOrigins;

    @Value("${cors.methods}")
    private String corsMethods;

    @Value("${cors.headers}")
    private String corsHeaders;

    @Value("${cors.credentials:true}")
    private boolean corsCredentials;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(corsOrigins.split(","))
                .allowedMethods(corsMethods.split(","))
                .allowedHeaders(corsHeaders.split(","))
                .allowCredentials(corsCredentials);
    }
}
