package org.selenium.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.selenium.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class FirefoxDriverManager implements DriverManager {

    private static final Logger log = LoggerFactory.getLogger(FirefoxDriverManager.class);

    public WebDriver getLocalDriver() {
        return new FirefoxDriver(getFirefoxOptionsOptions());
    }

    public WebDriver getRemoteDriver() throws MalformedURLException {
        String url = Config.HUB_URL.getProperty();
        log.info("Grid url: {}", url);
        return new RemoteWebDriver(new URL(url), getFirefoxOptionsOptions());
    }

    private FirefoxOptions getFirefoxOptionsOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (Boolean.parseBoolean(Config.HEADLESS.getProperty())) {
            firefoxOptions.addArguments("--headless");
        }
        firefoxOptions.addArguments("--incognito");
        return firefoxOptions;
    }

}
