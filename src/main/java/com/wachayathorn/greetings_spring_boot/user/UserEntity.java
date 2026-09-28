package com.wachayathorn.greetings_spring_boot.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
public record UserEntity(
        @Id Long id,
        String name,
        String email,
        String password) {
}