package com.wachayathorn.greetings_spring_boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.error")
public record ErrorProperties(boolean includeDetails) {
}
