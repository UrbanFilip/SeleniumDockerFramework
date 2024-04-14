package org.selenium.tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.selenium.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;


public class TwoTests extends BaseTest {


    @Severity(SeverityLevel.BLOCKER)
    @Test
    public void checkIfMainPageLoaded() {
        Assert.assertEquals(driver.getTitle(), "Google", "Page title is not correct.");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test(dependsOnMethods = "checkIfMainPageLoaded")
    public void checkIfLoginButtonRedirectsToLoginPage() {
        LoginPage loginPage = mainPage.clickLoginButton();
        Assert.assertTrue(loginPage.isDisplayed(), "Login page is not loaded.");
    }

}
