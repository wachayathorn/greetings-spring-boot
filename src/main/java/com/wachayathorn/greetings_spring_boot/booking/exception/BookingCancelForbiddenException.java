package com.wachayathorn.greetings_spring_boot.booking.exception;

public class BookingCancelForbiddenException extends RuntimeException {

    public BookingCancelForbiddenException() {
        super("Cannot cancel this booking");
    }
}
