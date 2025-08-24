package org.selenium.driverfactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.selenium.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class ChromeDriverManager implements DriverManager {
    private static final Logger log = LoggerFactory.getLogger(ChromeDriverManager.class);

    public WebDriver getLocalDriver() {
        log.info("Local driver");
        return new ChromeDriver(getChromeOptions());
    }

    public WebDriver getRemoteDriver() throws MalformedURLException {
        String url = Config.HUB_URL.getProperty();
        log.info("Grid url: {}", url);
        return new RemoteWebDriver(new URL(url), getChromeOptions());
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        if (Boolean.parseBoolean(Config.HEADLESS.getProperty())) {
            chromeOptions.addArguments("headless");
        }
        chromeOptions.addArguments("--incognito");
        return chromeOptions;
    }
}
