package com.wachayathorn.greetings_spring_boot.booking.exception;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException() {
        super("Booking not found");
    }
}
