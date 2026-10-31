package com.wachayathorn.greetings_spring_boot.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wachayathorn.greetings_spring_boot.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUsernameNormalized(String usernameNormalized);

    Optional<UserEntity> findByUsernameNormalized(String usernameNormalized);
}
