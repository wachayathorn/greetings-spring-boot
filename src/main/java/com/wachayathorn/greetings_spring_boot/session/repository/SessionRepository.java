package com.wachayathorn.greetings_spring_boot.session.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wachayathorn.greetings_spring_boot.session.entity.SessionEntity;

public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {
}
