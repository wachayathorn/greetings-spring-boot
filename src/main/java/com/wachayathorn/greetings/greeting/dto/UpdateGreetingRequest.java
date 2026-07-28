package com.wachayathorn.greetings.greeting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateGreetingRequest(
        @NotBlank @Size(max = 100) String name
) {
}
