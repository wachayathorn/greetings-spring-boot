package com.wachayathorn.greetings_spring_boot.session.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Size(min = 3, max = 32) @Pattern(regexp = "^[A-Za-z0-9_]+$") String username,
        @NotBlank @Size(min = 8, max = 72) String password) {
}
