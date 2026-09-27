package com.wachayathorn.greetings_spring_boot.user.dto.response;

import com.wachayathorn.greetings_spring_boot.user.UserEntity;

public class CreateUserRes {
    private String id;
    private String name;
    private String email;
    private String password;

    public CreateUserRes(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static CreateUserRes fromEntity(UserEntity entity) {
        return new CreateUserRes(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
