package com.wachayathorn.greetings_spring_boot.room.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.wachayathorn.greetings_spring_boot.room.entity.RoomEntity;
import com.wachayathorn.greetings_spring_boot.room.repository.RoomRepository;

@Component
public class RoomSeedConfig implements ApplicationRunner {

    private static final String[] ROOM_NAMES = {"Sirius", "Canopus", "Vega", "Rigel", "Polaris"};

    private final RoomRepository roomRepository;

    public RoomSeedConfig(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (roomRepository.count() > 0) {
            return;
        }
        for (String name : ROOM_NAMES) {
            roomRepository.save(new RoomEntity(name));
        }
    }
}
