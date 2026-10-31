package com.wachayathorn.greetings_spring_boot.config;

import java.time.ZoneId;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.time")
public record AppTimeProperties(ZoneId zone) {

    public AppTimeProperties {
        if (zone == null) {
            zone = ZoneId.of("Asia/Bangkok");
        }
    }
}
