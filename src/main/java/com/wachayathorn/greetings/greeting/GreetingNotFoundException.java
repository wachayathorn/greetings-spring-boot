package com.wachayathorn.greetings.greeting;

public class GreetingNotFoundException extends RuntimeException {

    public GreetingNotFoundException(Long id) {
        super("Greeting not found: id=" + id);
    }
}
