package com.wachayathorn.greetings_spring_boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthyApplication {

    @GetMapping("/healthy")
    public String healthy() {
        return "Healthy";
    }
}
