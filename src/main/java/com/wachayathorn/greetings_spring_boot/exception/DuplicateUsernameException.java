package com.wachayathorn.greetings_spring_boot.exception;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException() {
        super("Username already taken");
    }
}
