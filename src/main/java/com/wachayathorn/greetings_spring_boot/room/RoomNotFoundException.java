package com.wachayathorn.greetings_spring_boot.room;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException() {
        super("Room not found");
    }
}
