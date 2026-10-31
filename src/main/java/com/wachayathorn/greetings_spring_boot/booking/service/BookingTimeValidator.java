package com.wachayathorn.greetings_spring_boot.booking.service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.wachayathorn.greetings_spring_boot.booking.exception.InvalidBookingTimeException;

@Service
public class BookingTimeValidator {

    private static final Duration MIN_DURATION = Duration.ofMinutes(15);
    private static final Duration MAX_DURATION = Duration.ofHours(8);

    private final Clock clock;

    public BookingTimeValidator(Clock clock) {
        this.clock = clock;
    }

    public void validate(LocalDateTime start, LocalDateTime end) {
        if (!isWholeMinute(start) || !isWholeMinute(end)) {
            throw new InvalidBookingTimeException("Times must be whole minutes");
        }
        if (!end.isAfter(start)) {
            throw new InvalidBookingTimeException("End must be after start");
        }

        Duration length = Duration.between(start, end);
        if (length.compareTo(MIN_DURATION) < 0) {
            throw new InvalidBookingTimeException("Booking must be at least 15 minutes");
        }
        if (length.compareTo(MAX_DURATION) > 0) {
            throw new InvalidBookingTimeException("Booking must be at most 8 hours");
        }

        LocalDateTime currentMinute = LocalDateTime.now(clock).truncatedTo(ChronoUnit.MINUTES);
        if (start.isBefore(currentMinute)) {
            throw new InvalidBookingTimeException("Start must be the current minute or later");
        }
    }

    private static boolean isWholeMinute(LocalDateTime value) {
        return value.getSecond() == 0 && value.getNano() == 0;
    }
}
