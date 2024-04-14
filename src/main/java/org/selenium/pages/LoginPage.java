package org.selenium.pages;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.selenium.driverfactory.ChromeDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends MainPage {

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
    public static final String EMAIL_INPUT_CSS = "input[type='email']";

    @FindBy(css = EMAIL_INPUT_CSS)
    private WebElement email;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDisplayed() {
        log.info("Is login page displayed");
        try {
            wait.until(driver1 -> email.isDisplayed());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

}
