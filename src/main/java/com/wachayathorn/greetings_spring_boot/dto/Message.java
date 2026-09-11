package com.wachayathorn.greetings_spring_boot.dto;

import java.time.LocalDateTime;

public class Message {
    private String message;
    private LocalDateTime localTimestamp;

    public Message(String message) {
        this.message = message;
        this.localTimestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getLocalTimestamp() {
        return localTimestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLocalTimestamp(LocalDateTime localTimestamp) {
        this.localTimestamp = localTimestamp;
    }
}
