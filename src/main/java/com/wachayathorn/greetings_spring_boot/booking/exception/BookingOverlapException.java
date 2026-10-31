package com.wachayathorn.greetings_spring_boot.booking.exception;

public class BookingOverlapException extends RuntimeException {

    public BookingOverlapException() {
        super("Booking overlaps an existing hold");
    }
}
