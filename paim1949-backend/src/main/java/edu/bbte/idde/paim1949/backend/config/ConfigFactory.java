package edu.bbte.idde.paim1949.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;

public class ConfigFactory {
    private static Config config;

    public static synchronized Config getConfig() {
        if (config == null) {
            try {
                InputStream inputStream = Config.class.getResourceAsStream("/application.yml");
                config = new ObjectMapper(new YAMLFactory()).readValue(inputStream, Config.class);
            } catch (IOException ex) {
                config = new Config();
            }
        }
        return config;
    }

}