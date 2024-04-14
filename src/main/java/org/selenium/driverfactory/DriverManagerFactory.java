package org.selenium.driverfactory;

import org.openqa.selenium.WebDriver;
import org.selenium.constants.BrowserType;
import org.selenium.utils.Config;

import java.net.MalformedURLException;

public class DriverManagerFactory {

    public static WebDriver getLocalDriver() {
        String browser = System.getProperty("browser", Config.BROWSER.getProperty());
        return switch (BrowserType.valueOf(browser.toUpperCase())) {
            case CHROME -> new ChromeDriverManager().getLocalDriver();
            case FIREFOX -> new FirefoxDriverManager().getLocalDriver();
        };
    }

    public static WebDriver getRemoteDriver() throws MalformedURLException {
        String browser = System.getProperty("browser", Config.BROWSER.getProperty());
        return switch (BrowserType.valueOf(browser.toUpperCase())) {
            case CHROME -> new ChromeDriverManager().getRemoteDriver();
            case FIREFOX -> new FirefoxDriverManager().getRemoteDriver();
        };
    }
}
