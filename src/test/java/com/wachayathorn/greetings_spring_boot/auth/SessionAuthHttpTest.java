package com.wachayathorn.greetings_spring_boot.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.wachayathorn.greetings_spring_boot.support.MeetingRoomsHttpSupport;
import com.wachayathorn.greetings_spring_boot.support.MutableThailandClock;

@SpringBootTest
@AutoConfigureMockMvc
class SessionAuthHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginAcceptsRegistrationSpellingAndDifferentCase() throws Exception {
        MeetingRoomsHttpSupport.register(mockMvc, "LoginAnn", "password1");
        MeetingRoomsHttpSupport.login(mockMvc, "LoginAnn", "password1");
        MeetingRoomsHttpSupport.login(mockMvc, "loginann", "password1");
    }

    @Test
    void loginRejectsWrongPasswordOrUnknownUser() throws Exception {
        MeetingRoomsHttpSupport.register(mockMvc, "known_auth", "password1");

        mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"known_auth","password":"wrongpass"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist());

        mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"nobody_here","password":"password1"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    void protectedRequestRejectedWithoutUsableToken() throws Exception {
        mockMvc.perform(get("/api/v1/rooms")).andExpect(status().isUnauthorized());

        String token = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "rooms_auth", "password1");
        MeetingRoomsHttpSupport.logout(mockMvc, token);

        mockMvc.perform(get("/api/v1/rooms").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void twoLoginsStayIndependentUntilEachLogout() throws Exception {
        MeetingRoomsHttpSupport.register(mockMvc, "dual_login", "password1");
        String first = MeetingRoomsHttpSupport.login(mockMvc, "dual_login", "password1");
        String second = MeetingRoomsHttpSupport.login(mockMvc, "dual_login", "password1");

        mockMvc.perform(get("/api/v1/rooms").header("Authorization", "Bearer " + first))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/rooms").header("Authorization", "Bearer " + second))
                .andExpect(status().isOk());

        MeetingRoomsHttpSupport.logout(mockMvc, first);

        mockMvc.perform(get("/api/v1/rooms").header("Authorization", "Bearer " + first))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/rooms").header("Authorization", "Bearer " + second))
                .andExpect(status().isOk());
    }

    @Test
    void secondLogoutWithSameTokenIsRejected() throws Exception {
        MeetingRoomsHttpSupport.register(mockMvc, "logout_twice", "password1");
        String token = MeetingRoomsHttpSupport.login(mockMvc, "logout_twice", "password1");
        MeetingRoomsHttpSupport.logout(mockMvc, token);

        mockMvc.perform(delete("/api/v1/sessions").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void swaggerUiDoesNotRequireLogin() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
        mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
    }

    @Test
    void noRefreshEndpointExists() throws Exception {
        mockMvc.perform(post("/api/v1/sessions/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }
}

@SpringBootTest(properties = "app.auth.session-duration=PT24H")
@AutoConfigureMockMvc
@Import(SessionExpiryHttpTest.ClockOverride.class)
class SessionExpiryHttpTest {

    static MutableThailandClock clock =
            new MutableThailandClock(Instant.parse("2026-10-31T03:00:00+07:00"));

    @TestConfiguration
    static class ClockOverride {
        @Bean
        @Primary
        java.time.Clock clock() {
            return clock;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginOlderThanTwentyFourHoursIsRejected() throws Exception {
        clock.setInstant(Instant.parse("2026-10-31T03:00:00+07:00"));
        MeetingRoomsHttpSupport.register(mockMvc, "expiry_user", "password1");
        String token = MeetingRoomsHttpSupport.login(mockMvc, "expiry_user", "password1");

        clock.setInstant(clock.instant().plus(24, ChronoUnit.HOURS).plusSeconds(1));

        mockMvc.perform(get("/api/v1/rooms").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }
}
