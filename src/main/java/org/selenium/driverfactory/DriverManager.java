package org.selenium.driverfactory;

import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;


public interface DriverManager {

    WebDriver getLocalDriver();

    WebDriver getRemoteDriver() throws MalformedURLException;
}
