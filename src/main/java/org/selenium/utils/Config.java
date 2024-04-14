package org.selenium.utils;


import org.selenium.constants.Env;
import java.util.Properties;

import static org.selenium.constants.Env.*;

public enum Config {

    GRID_HUB_URL,
    BROWSER,
    URL,
    HEADLESS,
    REMOTE,
    REGULAR_WAIT;

    private final Properties properties;

    Config() {
        String env = System.getProperty("env", STAGING.getEnvType());
        String configPath = "environments/%s.properties";
        switch (Env.valueOf(env.toUpperCase())) {
            case STAGING ->
                    this.properties = PropertyLoader.propertyLoader(String.format(configPath, STAGING.getEnvType()));
            case PROD ->
                    this.properties = PropertyLoader.propertyLoader(String.format(configPath, PROD.getEnvType()));
            case QA ->
                    this.properties = PropertyLoader.propertyLoader(String.format(configPath, QA.getEnvType()));
            case DEV ->
                    this.properties = PropertyLoader.propertyLoader(String.format(configPath, DEV.getEnvType()));
            default -> throw new IllegalStateException("Invalid env type: " + env);
        }
    }

    public String getProperty() {
        return properties.getProperty(this.toString());
    }


}
