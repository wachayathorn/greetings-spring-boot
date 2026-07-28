package com.wachayathorn.greetings.greeting.dto;

import java.time.Instant;

public record GreetingResponse(
        Long id,
        String name,
        Instant createdAt
) {
}
