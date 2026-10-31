package com.wachayathorn.greetings_spring_boot.room.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wachayathorn.greetings_spring_boot.room.entity.RoomEntity;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Optional<RoomEntity> findByNameIgnoreCase(String name);
}
