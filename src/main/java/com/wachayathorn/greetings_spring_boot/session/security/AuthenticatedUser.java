package com.wachayathorn.greetings_spring_boot.session.security;

import com.wachayathorn.greetings_spring_boot.user.entity.UserEntity;

public record AuthenticatedUser(Long userId, String username) {

    public static AuthenticatedUser from(UserEntity user) {
        return new AuthenticatedUser(user.getId(), user.getUsername());
    }
}
