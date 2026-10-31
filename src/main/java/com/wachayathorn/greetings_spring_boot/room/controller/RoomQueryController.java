package com.wachayathorn.greetings_spring_boot.room.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wachayathorn.greetings_spring_boot.room.dto.response.RoomResponse;
import com.wachayathorn.greetings_spring_boot.room.service.RoomService;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomQueryController {

    private final RoomService roomService;

    public RoomQueryController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> listRooms() {
        List<RoomResponse> rooms = roomService.listRooms();
        return ResponseEntity.status(HttpStatus.OK).body(rooms);
    }
}
