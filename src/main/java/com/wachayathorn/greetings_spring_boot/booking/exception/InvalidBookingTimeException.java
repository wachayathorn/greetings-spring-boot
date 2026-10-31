package com.wachayathorn.greetings_spring_boot.booking.exception;

public class InvalidBookingTimeException extends RuntimeException {

    public InvalidBookingTimeException(String message) {
        super(message);
    }
}
