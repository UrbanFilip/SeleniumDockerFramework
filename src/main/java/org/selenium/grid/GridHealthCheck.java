package org.selenium.grid;

import org.selenium.driverfactory.FirefoxDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public class GridHealthCheck {

    private static final Logger log = LoggerFactory.getLogger(GridHealthCheck.class);

    public static void waitForGrid() throws IOException, InterruptedException {
        log.info("Check if grid is up");
        Runtime runtime = Runtime.getRuntime();
        boolean hubResponse = false;
        int counter = 0;
        do {
            BufferedReader bufferedReader = runtime.exec("curl http://localhost:4444/status").inputReader();
            Optional<String> value = bufferedReader.lines()
                    .filter(s -> s.contains("\"ready\": " + true))
                    .findFirst();
            if (value.isPresent()) {
                hubResponse = true;
                bufferedReader.close();
                log.info("Grid is up");
                break;
            }
            bufferedReader.close();
            Thread.sleep(1000);
            log.info("Checked: " + counter++ + " times.");
        } while (counter < 30);
        if (!hubResponse)
            System.out.println("Grid did not start in time");
        Assert.assertTrue(hubResponse);
    }

}
