package org.selenium.tests;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.selenium.driverfactory.DriverManagerFactory;
import org.selenium.grid.GridHealthCheck;
import org.selenium.pages.MainPage;
import org.selenium.utils.Config;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;

public class BaseTest {
    protected WebDriver driver;
    protected MainPage mainPage;

    @BeforeClass
    public void startDriverAndOpenBrowser(ITestContext context) throws IOException, InterruptedException {
        boolean remote = Boolean.parseBoolean(System.getProperty("remote", Config.REMOTE.getProperty()));
        driver = remote ? DriverManagerFactory.getRemoteDriver() : DriverManagerFactory.getLocalDriver();
        if (remote)
            GridHealthCheck.waitForGrid();
        openBrowser();
    }

    @AfterMethod
    public void takeScreenshotIfFailed(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {
            File destFile = new File("screenshot/" + result.getTestClass().getRealClass().getSimpleName() + "_" + result.getMethod().getMethodName() + ".png");
            takeScreenshot(destFile);
            saveScreenshotPng();
        }
    }

    @AfterClass
    public void quitDriver() {
        driver.quit();
    }


    @Attachment(value = "Page screenshot", type = "image/png")
    private void saveScreenshotPng() {
        ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    private void takeScreenshot(File destFile) throws IOException {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, destFile);
    }

    private void openBrowser() {
        mainPage = new MainPage(driver)
                .load();
        driver.manage().window().maximize();
    }

}
