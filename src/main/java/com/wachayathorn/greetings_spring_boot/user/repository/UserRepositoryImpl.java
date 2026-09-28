package com.wachayathorn.greetings_spring_boot.user.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wachayathorn.greetings_spring_boot.user.UserEntity;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        var sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        var id = jdbcTemplate.queryForObject(sql, Long.class, userEntity.name(), userEntity.email(),
                userEntity.password());
        return new UserEntity(id, userEntity.name(), userEntity.email(), userEntity.password());
    }
}
