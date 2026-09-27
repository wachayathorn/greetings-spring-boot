package com.wachayathorn.greetings_spring_boot.user;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.wachayathorn.greetings_spring_boot.user.dto.request.CreateUserReq;

@Table(name = "users")
public record UserEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id,
        @Column(name = "name", nullable = false, length = 100) String name,
        @Column(name = "email", nullable = false, length = 100) String email,
        @Column(name = "password", nullable = false, length = 100) String password) {
    public static UserEntity from(CreateUserReq createUserReq) {
        return new UserEntity(
                null,
                createUserReq.name(),
                createUserReq.email(),
                createUserReq.password());
    }
}
