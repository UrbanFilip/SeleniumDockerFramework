package org.selenium.constants;

import lombok.Getter;

public enum Env {

    STAGING("staging"),
    PROD("prod"),
    QA("qa"),
    DEV("dev");

    @Getter
    private final String envType;

    Env(String envName) {
        this.envType = envName;
    }
}
