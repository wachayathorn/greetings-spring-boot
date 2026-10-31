package com.wachayathorn.greetings_spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GreetingsSpringBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(GreetingsSpringBootApplication.class, args);
	}
}
