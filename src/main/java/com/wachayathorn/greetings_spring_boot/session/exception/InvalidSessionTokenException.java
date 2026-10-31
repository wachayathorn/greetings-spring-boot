package com.wachayathorn.greetings_spring_boot.session.exception;

public class InvalidSessionTokenException extends RuntimeException {

    public InvalidSessionTokenException() {
        super("Invalid or expired session");
    }
}
