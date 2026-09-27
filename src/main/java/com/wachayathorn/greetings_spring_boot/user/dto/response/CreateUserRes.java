package com.wachayathorn.greetings_spring_boot.user.dto.response;

import com.wachayathorn.greetings_spring_boot.user.UserEntity;

public record CreateUserRes(
                Long id,
                String name,
                String email,
                String password) {
        public static CreateUserRes from(UserEntity entity) {
                return new CreateUserRes(
                                entity.id(),
                                entity.name(),
                                entity.email(),
                                entity.password());
        }
}
