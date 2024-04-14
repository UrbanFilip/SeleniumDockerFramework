package org.selenium.tests;

import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.selenium.driverfactory.DriverManagerFactory;
import org.selenium.utils.Config;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class BaseTest {
    protected WebDriver driver;

    @BeforeClass
    public void startDriver(ITestContext context) throws MalformedURLException {
        driver = Boolean.parseBoolean(System.getProperty("remote", Config.REMOTE.getProperty())) ? DriverManagerFactory.getRemoteDriver() : DriverManagerFactory.getLocalDriver();
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
}
