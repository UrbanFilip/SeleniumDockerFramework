package org.selenium.tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.selenium.pages.MainPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class OneTest extends BaseTest {


    @BeforeClass
    public void initializeMainPage() {
        new MainPage(driver)
                .load();
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test
    public void checkIfMainPageLoaded() {
        Assert.assertEquals(driver.getTitle(), "Google", "Page title is not correct.");
    }

}
