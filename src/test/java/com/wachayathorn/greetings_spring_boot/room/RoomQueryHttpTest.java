package com.wachayathorn.greetings_spring_boot.room;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.wachayathorn.greetings_spring_boot.support.MeetingRoomsHttpSupport;

@SpringBootTest
@AutoConfigureMockMvc
class RoomQueryHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listsFiveFixedRoomsWhenLoggedIn() throws Exception {
        String token = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "room_lister", "password1");

        mockMvc.perform(get("/api/v1/rooms").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[?(@.name == 'Sirius')]").exists())
                .andExpect(jsonPath("$[?(@.name == 'Canopus')]").exists())
                .andExpect(jsonPath("$[?(@.name == 'Vega')]").exists())
                .andExpect(jsonPath("$[?(@.name == 'Rigel')]").exists())
                .andExpect(jsonPath("$[?(@.name == 'Polaris')]").exists());
    }

    @Test
    void rejectsUnauthenticatedRoomList() throws Exception {
        mockMvc.perform(get("/api/v1/rooms")).andExpect(status().isUnauthorized());
    }

    @Test
    void hasNoRoomMutationEndpoint() throws Exception {
        String token = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "room_mutator", "password1");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/v1/rooms")
                        .header("Authorization", "Bearer " + token)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"NewRoom"}
                                """))
                .andExpect(status().isNotFound());
    }
}
