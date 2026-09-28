package com.wachayathorn.greetings_spring_boot.user.repository;

import com.wachayathorn.greetings_spring_boot.user.UserEntity;

public interface UserRepository {
    UserEntity save(UserEntity userEntity);
}
