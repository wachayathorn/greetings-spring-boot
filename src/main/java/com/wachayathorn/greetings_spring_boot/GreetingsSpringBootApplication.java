package com.wachayathorn.greetings_spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Greetings Spring Boot API",
        version = "1.0",
        description = "API documentation for Greetings Spring Boot service"
    )
)
@EnableAsync
public class GreetingsSpringBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(GreetingsSpringBootApplication.class, args);
	}
}
