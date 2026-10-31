package com.wachayathorn.greetings_spring_boot.room.service;

import java.util.List;

import com.wachayathorn.greetings_spring_boot.room.dto.response.RoomResponse;

public interface RoomService {

    List<RoomResponse> listRooms();
}
