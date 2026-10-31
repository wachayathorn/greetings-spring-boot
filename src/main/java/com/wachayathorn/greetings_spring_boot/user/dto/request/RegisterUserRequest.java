package com.wachayathorn.greetings_spring_boot.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
                @NotBlank @Pattern(regexp = "^[A-Za-z0-9_]{3,32}$", message = "must be 3 to 32 letters, digits, or underscores") String username,
                @NotBlank @Size(min = 8, max = 72) String password) {
}
