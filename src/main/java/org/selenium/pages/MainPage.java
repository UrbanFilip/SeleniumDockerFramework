package org.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.selenium.driverfactory.ChromeDriverManager;
import org.selenium.pages.base.BasePage;
import org.selenium.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(MainPage.class);
    private static final String ACCEPT_COOKIES_BUTTON_CSS = "#L2AGLb";
    private static final String LOGIN_BUTTON = "a[href*='Login']";

    @FindBy(css = ACCEPT_COOKIES_BUTTON_CSS)
    private WebElement acceptCookiesButton;

    @FindBy(css = LOGIN_BUTTON)
    private WebElement loginButton;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public MainPage load() {
        log.info("Open main page");
        driver.get(Config.URL.getProperty());
        closeCookies();
        return this;
    }

    private void closeCookies() {
        log.info("Close cookies");
        wait.until(ExpectedConditions.elementToBeClickable(acceptCookiesButton));
        acceptCookiesButton.click();
        wait.until(ExpectedConditions.invisibilityOf(acceptCookiesButton));
    }

    public LoginPage clickLoginButton() {
        log.info("Click on login button");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
        wait.until(ExpectedConditions.invisibilityOf(loginButton));
        return new LoginPage(driver);
    }

}
