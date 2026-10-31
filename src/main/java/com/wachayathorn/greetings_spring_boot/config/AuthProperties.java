package com.wachayathorn.greetings_spring_boot.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.auth")
public record AuthProperties(String jwtSecret, Duration sessionDuration) {

    public AuthProperties {
        if (sessionDuration == null) {
            sessionDuration = Duration.ofHours(24);
        }
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalArgumentException("app.auth.jwt-secret must be set");
        }
    }
}
