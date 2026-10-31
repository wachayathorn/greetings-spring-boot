package com.wachayathorn.greetings_spring_boot.booking.exception;

public class BookingAlreadyCancelledException extends RuntimeException {

    public BookingAlreadyCancelledException() {
        super("Booking already cancelled");
    }
}
