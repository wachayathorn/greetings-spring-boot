package com.wachayathorn.greetings_spring_boot.user.dto.response;

import com.wachayathorn.greetings_spring_boot.user.UserEntity;

public class CreateUserRes {
    private Long id;
    private String name;
    private String email;
    private String password;

    public CreateUserRes(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static CreateUserRes fromEntity(UserEntity entity) {
        return new CreateUserRes(
                entity.id(),
                entity.name(),
                entity.email(),
                entity.password());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
