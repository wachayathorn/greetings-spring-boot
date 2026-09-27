package com.wachayathorn.greetings_spring_boot.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotBlank @Size(min = 3, max = 100) String name,
        @NotBlank @Email(message = "Invalid email address") String email,
        @NotBlank @Size(min = 8, max = 100) String password) {
}