package org.selenium;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.regex.Pattern;

public class GridHealthCheck {

    private static final Pattern READY_PATTERN = Pattern.compile("\"ready\"\\s*:\\s*true", Pattern.CASE_INSENSITIVE);

    public static void main(String[] args) throws Exception {
        String statusUrl = args.length > 0 ? args[0] : "http://localhost:4444/status";
        int timeoutSec = args.length > 1 ? Integer.parseInt(args[1]) : 90;
        int intervalMs = args.length > 2 ? Integer.parseInt(args[2]) : 1000;

        long deadline = System.nanoTime() + Duration.ofSeconds(timeoutSec).toNanos();
        int attempts = 0;

        System.out.printf("[INFO] Waiting for Grid %s (timeout: %ds, interval: %dms)%n",
                statusUrl, timeoutSec, intervalMs);

        while (System.nanoTime() < deadline) {
            attempts++;
            try {
                if (isReady(statusUrl)) {
                    System.out.printf("[OK] Grid is ready after %d attempt(s).%n", attempts);
                    System.exit(0);
                } else {
                    System.out.printf("[INFO] Not ready yet... attempt=%d%n", attempts);
                }
            } catch (Exception e) {
                System.out.printf("[INFO] No response yet (%s) ... attempt=%d%n", e.getClass().getSimpleName(), attempts);
            }
            Thread.sleep(intervalMs);
        }
        System.err.printf("[ERROR] Grid not ready within %d seconds.%n", timeoutSec);
        System.exit(1);
    }

    private static boolean isReady(String statusUrl) throws Exception {
        URL url = new URL(statusUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        int code = conn.getResponseCode();
        if (code >= 200 && code < 300) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (READY_PATTERN.matcher(line).find()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
