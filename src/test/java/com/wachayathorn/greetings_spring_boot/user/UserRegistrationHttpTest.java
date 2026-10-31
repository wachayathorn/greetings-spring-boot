package com.wachayathorn.greetings_spring_boot.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class UserRegistrationHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerSucceedsAndReturnsSubmittedUsernameSpelling() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"SpellingUser","password":"password1"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("SpellingUser"));
    }

    @Test
    void registerDoesNotGrantSession() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"session_user","password":"password1"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(result.getResponse().getHeader("Set-Cookie")).isNull();
        assertThat(result.getResponse().getContentAsString()).doesNotContain("token");
        assertThat(result.getResponse().getContentAsString()).doesNotContain("access");
    }

    @Test
    void registerRejectsUsernameThatDiffersOnlyByLetterCase() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"Ann","password":"password1"}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"ann","password":"password2"}
                                """))
                .andExpect(status().isConflict());

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"Ann","password":"password3"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void registerRejectsInvalidUsername() throws Exception {
        assertBadRequest("""
                {"username":"ab","password":"password1"}
                """);
        assertBadRequest("""
                {"username":"%s","password":"password1"}
                """.formatted("a".repeat(33)));
        assertBadRequest("""
                {"username":"bad-name","password":"password1"}
                """);
    }

    @Test
    void registerRejectsInvalidPassword() throws Exception {
        assertBadRequest("""
                {"username":"valid_user","password":"short"}
                """);
        assertBadRequest("""
                {"username":"valid_user","password":"%s"}
                """.formatted("x".repeat(73)));
    }

    @Test
    void registerRejectsMissingFields() throws Exception {
        assertBadRequest("""
                {"password":"password1"}
                """);
        assertBadRequest("""
                {"username":"valid_user"}
                """);
    }

    private void assertBadRequest(String body) throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserRegistrationRestartHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void registerBeforeRestart() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"restart_user","password":"password1"}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void registerSameUsernameAfterRestart() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"restart_user","password":"password1"}
                                """))
                .andExpect(status().isCreated());
    }
}
