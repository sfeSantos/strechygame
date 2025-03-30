package edu.scratchy.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import edu.scratchy.models.GameConfig;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public GameConfig loadConfig(String configPath) {
        try {
            return OBJECT_MAPPER.readValue(new File(configPath), GameConfig.class);
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load game configuration", e);
        }
    }

    public static class ConfigurationException extends RuntimeException {
        public ConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
