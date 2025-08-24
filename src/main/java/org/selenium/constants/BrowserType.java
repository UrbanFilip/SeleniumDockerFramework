package org.selenium.constants;

import lombok.Getter;

public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox");

    @Getter
    private final String browserName;

    BrowserType(String browserName) {
        this.browserName = browserName;
    }
}
