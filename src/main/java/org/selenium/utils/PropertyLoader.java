package org.selenium.utils;

import org.selenium.driverfactory.ChromeDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
    private static final Logger log = LoggerFactory.getLogger(PropertyLoader.class);

    public static Properties propertyLoader(String filePath) {
        Properties properties = new Properties();
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            try {
                properties.load(reader);
                reader.close();
                log.info("Properties from: " + filePath + " have been loaded");
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load properties file: " + filePath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Properties file not found at: " + filePath);
        }
        return properties;
    }
}
