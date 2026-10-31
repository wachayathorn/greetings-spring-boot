package com.wachayathorn.greetings_spring_boot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(name = "users_username_normalized_unique", columnNames = "username_normalized"))
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String username;

    @Column(name = "username_normalized", nullable = false, length = 32)
    private String usernameNormalized;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    protected UserEntity() {
    }

    public UserEntity(String username, String usernameNormalized, String passwordHash) {
        this.username = username;
        this.usernameNormalized = usernameNormalized;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUsernameNormalized() {
        return usernameNormalized;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
