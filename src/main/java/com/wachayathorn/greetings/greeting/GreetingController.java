package com.wachayathorn.greetings.greeting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/greetings")
public class GreetingController {

    private final GreetingService greetingService;

    // Constructor injection — Spring สร้าง bean ให้แล้ว inject เข้ามา (เทียบ NestJS constructor DI)
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping
    public GreetingResponse greet(
            @RequestParam(defaultValue = "World") String name
    ) {
        return greetingService.greet(name);
    }
}
