package com.wachayathorn.greetings_spring_boot.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Meeting rooms",
                version = "1.0",
                description = "Book one of five named rooms"
        )
)
public class OpenApiConfig {
}
