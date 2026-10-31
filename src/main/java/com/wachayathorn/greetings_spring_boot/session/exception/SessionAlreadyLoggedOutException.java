package com.wachayathorn.greetings_spring_boot.session.exception;

public class SessionAlreadyLoggedOutException extends RuntimeException {

    public SessionAlreadyLoggedOutException() {
        super("Session already logged out");
    }
}
