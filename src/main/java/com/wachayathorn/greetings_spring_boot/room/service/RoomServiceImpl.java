package com.wachayathorn.greetings_spring_boot.room.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wachayathorn.greetings_spring_boot.room.repository.RoomRepository;
import com.wachayathorn.greetings_spring_boot.room.dto.response.RoomResponse;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> listRooms() {
        return roomRepository.findAll().stream()
                .sorted(Comparator.comparing(room -> room.getName()))
                .map(room -> new RoomResponse(room.getName()))
                .toList();
    }
}
