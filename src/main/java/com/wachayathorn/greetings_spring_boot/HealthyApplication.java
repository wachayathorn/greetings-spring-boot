package com.wachayathorn.greetings_spring_boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wachayathorn.greetings_spring_boot.dto.Message;

@RestController
public class HealthyApplication {

    @GetMapping("/healthy")
    public Message healthy() {
        return new Message("Healthy");
    }
}
