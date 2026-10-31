package com.wachayathorn.greetings_spring_boot.support;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

public final class MeetingRoomsHttpSupport {

    private MeetingRoomsHttpSupport() {
    }

    public static void register(MockMvc mockMvc, String username, String password) throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"%s","password":"%s"}
                                """.formatted(username, password)))
                .andExpect(status().isCreated());
    }

    public static String login(MockMvc mockMvc, String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"%s","password":"%s"}
                                """.formatted(username, password)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    public static void logout(MockMvc mockMvc, String token) throws Exception {
        mockMvc.perform(delete("/api/v1/sessions").header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    public static String registerAndLogin(MockMvc mockMvc, String username, String password) throws Exception {
        register(mockMvc, username, password);
        return login(mockMvc, username, password);
    }
}
