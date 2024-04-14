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
        WebDriver driver = new FirefoxDriver(getFirefoxOptionsOptions());
        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver getRemoteDriver() throws MalformedURLException {
        String url = Config.URL.getProperty();
        log.info("Grid url: {}", url);
        WebDriver driver = new RemoteWebDriver(new URL(url), getFirefoxOptionsOptions());
        driver.manage().window().maximize();
        return driver;
    }

    private FirefoxOptions getFirefoxOptionsOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        if (Boolean.parseBoolean(Config.HEADLESS.getProperty()))
            firefoxOptions.addArguments("--headless");
        firefoxOptions.addArguments("--incognito");
        return firefoxOptions;
    }

}
