package com.wachayathorn.greetings_spring_boot.booking.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingResponse(
        Long id,
        String room,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime start,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime end,
        String owner,
        Boolean cancelled) {

    public static BookingResponse active(
            Long id, String room, LocalDateTime start, LocalDateTime end, String owner) {
        return new BookingResponse(id, room, start, end, owner, null);
    }

    public static BookingResponse cancelled(
            Long id, String room, LocalDateTime start, LocalDateTime end, String owner) {
        return new BookingResponse(id, room, start, end, owner, true);
    }
}
