package edu.bbte.idde.paim1949.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ConfigFactory {
    private static final String CONFIG_FILE_NAME = "application";
    private static Config config;

    public static synchronized Config getConfig() {
        if (config == null) {
            String propertiesResourceName = buildConfigFileName();
            log.info("Attempting to load properties from {}", propertiesResourceName);

            try (InputStream inputStream = Config.class.getResourceAsStream(propertiesResourceName)) {
                config = new ObjectMapper(new YAMLFactory()).readValue(inputStream, Config.class);
            } catch (IOException e) {
                log.error("Error loading properties", e);
            }
        }
        return config;
    }

    private static String buildConfigFileName() {
        log.info("Loading configurations");
        StringBuilder configFileNameBuilder = new StringBuilder("/");
        configFileNameBuilder.append(CONFIG_FILE_NAME);
        String profile = System.getProperty("profile");
        log.info("Determined profile: {}", profile);
        if (profile != null && !profile.isEmpty()) {
            configFileNameBuilder.append('-').append(profile);
        }
        configFileNameBuilder.append(".yml");
        return configFileNameBuilder.toString();
    }
}