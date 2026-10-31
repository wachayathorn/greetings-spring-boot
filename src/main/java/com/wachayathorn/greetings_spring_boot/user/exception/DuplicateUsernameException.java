package com.wachayathorn.greetings_spring_boot.user.exception;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException() {
        super("Username already taken");
    }
}
