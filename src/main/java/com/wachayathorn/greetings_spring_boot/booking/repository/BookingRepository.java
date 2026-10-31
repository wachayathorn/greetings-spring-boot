package com.wachayathorn.greetings_spring_boot.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wachayathorn.greetings_spring_boot.booking.entity.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM BookingEntity b
            WHERE b.room.id = :roomId
              AND b.cancelledAt IS NULL
              AND b.startAt < :endAt
              AND b.endAt > :startAt
            """)
    boolean existsActiveRoomOverlap(
            @Param("roomId") Long roomId,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt);

    @Query("""
            SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
            FROM BookingEntity b
            WHERE b.user.id = :userId
              AND b.cancelledAt IS NULL
              AND b.startAt < :endAt
              AND b.endAt > :startAt
            """)
    boolean existsActiveUserOverlap(
            @Param("userId") Long userId,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt);

    @Query("""
            SELECT b FROM BookingEntity b
            JOIN FETCH b.room
            JOIN FETCH b.user
            WHERE b.cancelledAt IS NULL
               OR b.user.id = :viewerUserId
            ORDER BY b.startAt ASC, b.id ASC
            """)
    List<BookingEntity> findVisibleForUser(@Param("viewerUserId") Long viewerUserId);
}
