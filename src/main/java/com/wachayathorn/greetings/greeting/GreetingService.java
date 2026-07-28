package com.wachayathorn.greetings.greeting;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public GreetingResponse greet(String name) {
        return new GreetingResponse("Hello, " + name + "!");
    }
}
