package com.wachayathorn.greetings_spring_boot.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfig {

    @Bean
    @SuppressWarnings("unused")
    Clock clock(AppTimeProperties timeProperties) {
        return Clock.system(timeProperties.zone());
    }
}
