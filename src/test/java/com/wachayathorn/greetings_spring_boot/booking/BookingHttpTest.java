package com.wachayathorn.greetings_spring_boot.booking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;
import com.wachayathorn.greetings_spring_boot.support.MeetingRoomsHttpSupport;
import com.wachayathorn.greetings_spring_boot.support.MutableThailandClock;

@SpringBootTest
@AutoConfigureMockMvc
@Import(BookingHttpTest.ClockOverride.class)
class BookingHttpTest {

    static final MutableThailandClock CLOCK =
            new MutableThailandClock(Instant.parse("2026-10-31T10:00:00+07:00"));

    @TestConfiguration
    static class ClockOverride {
        @Bean
        @Primary
        java.time.Clock clock() {
            return CLOCK;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    private String ownerToken;

    @BeforeEach
    void setUp() throws Exception {
        CLOCK.setInstant(Instant.parse("2026-10-31T10:00:00+07:00"));
        ownerToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "BookingOwner", "password1");
    }

    @Test
    void createBookingWithinRules() throws Exception {
        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Sirius","start":"2026-10-31T10:00:00","end":"2026-10-31T10:30:00"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.room").value("Sirius"))
                .andExpect(jsonPath("$.start").value("2026-10-31T10:00:00"))
                .andExpect(jsonPath("$.end").value("2026-10-31T10:30:00"))
                .andExpect(jsonPath("$.owner").value("BookingOwner"))
                .andExpect(jsonPath("$.cancelled").doesNotExist());
    }

    @Test
    void overnightBookingSucceeds() throws Exception {
        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Vega","start":"2026-10-31T22:00:00","end":"2026-11-01T06:00:00"}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void adjacentBookingsOnSameRoomSucceed() throws Exception {
        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Rigel","start":"2026-10-31T10:00:00","end":"2026-10-31T11:00:00"}
                                """))
                .andExpect(status().isCreated());

        String otherToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "adjacent_user", "password1");
        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + otherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Rigel","start":"2026-10-31T11:00:00","end":"2026-10-31T12:00:00"}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void rejectsOverlappingRoomBooking() throws Exception {
        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Polaris","start":"2026-10-31T12:00:00","end":"2026-10-31T13:00:00"}
                                """))
                .andExpect(status().isCreated());

        String otherToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "overlap_room", "password1");
        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + otherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Polaris","start":"2026-10-31T12:30:00","end":"2026-10-31T13:30:00"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsOverlappingUserBookingAcrossRooms() throws Exception {
        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Sirius","start":"2026-10-31T14:00:00","end":"2026-10-31T15:00:00"}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Canopus","start":"2026-10-31T14:30:00","end":"2026-10-31T15:30:00"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsInvalidTimes() throws Exception {
        assertBadRequest("""
                {"room":"Sirius","start":"2026-10-31T09:59:00","end":"2026-10-31T10:14:00"}
                """);
        assertBadRequest("""
                {"room":"Sirius","start":"2026-10-31T10:00:00","end":"2026-10-31T10:14:00"}
                """);
        assertBadRequest("""
                {"room":"Sirius","start":"2026-10-31T10:00:00","end":"2026-10-31T18:01:00"}
                """);
        assertBadRequest("""
                {"room":"Sirius","start":"2026-10-31T11:00:00","end":"2026-10-31T11:00:00"}
                """);
        assertBadRequest("""
                {"room":"Sirius","start":"2026-10-31T10:00:30","end":"2026-10-31T10:30:00"}
                """);
    }

    @Test
    void rejectsUnknownRoom() throws Exception {
        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Andromeda","start":"2026-10-31T16:00:00","end":"2026-10-31T17:00:00"}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsUnauthenticatedCreate() throws Exception {
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Sirius","start":"2026-10-31T17:00:00","end":"2026-10-31T18:00:00"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    private void assertBadRequest(String body) throws Exception {
        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}

@SpringBootTest
@AutoConfigureMockMvc
@Import(BookingHttpTest.ClockOverride.class)
class BookingListHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listsActiveBookingsForEveryoneWithRegistrationSpelling() throws Exception {
        BookingHttpTest.CLOCK.setInstant(Instant.parse("2026-10-31T10:00:00+07:00"));
        MeetingRoomsHttpSupport.register(mockMvc, "ListAnn", "password1");
        String annToken = MeetingRoomsHttpSupport.login(mockMvc, "listann", "password1");
        String viewerToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "viewer_user", "password1");

        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + annToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Sirius","start":"2026-10-31T10:00:00","end":"2026-10-31T11:00:00"}
                                """))
                .andExpect(status().isCreated());

        BookingHttpTest.CLOCK.setInstant(Instant.parse("2026-10-31T12:00:00+07:00"));

        mockMvc.perform(get("/api/v1/bookings").header("Authorization", "Bearer " + viewerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].owner").value("ListAnn"))
                .andExpect(jsonPath("$[0].start").value("2026-10-31T10:00:00"))
                .andExpect(jsonPath("$[0].end").value("2026-10-31T11:00:00"));
    }

    @Test
    void rejectsUnauthenticatedList() throws Exception {
        mockMvc.perform(get("/api/v1/bookings")).andExpect(status().isUnauthorized());
    }

    @Test
    void hasNoFreeSlotEndpoint() throws Exception {
        String token = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "slot_seeker", "password1");
        mockMvc.perform(get("/api/v1/rooms/Sirius/free-slots").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}

@SpringBootTest
@AutoConfigureMockMvc
@Import(BookingHttpTest.ClockOverride.class)
class BookingCancelHttpTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void ownerCancelFreesRoomForOthers() throws Exception {
        BookingHttpTest.CLOCK.setInstant(Instant.parse("2026-10-31T10:00:00+07:00"));
        String ownerToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "CancelOwner", "password1");
        String otherToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "CancelOther", "password1");

        MvcResult created = mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Canopus","start":"2026-10-31T10:00:00","end":"2026-10-31T12:00:00"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        long bookingId = JsonPath.read(created.getResponse().getContentAsString(), "$.id");

        BookingHttpTest.CLOCK.setInstant(Instant.parse("2026-10-31T10:30:00+07:00"));

        mockMvc.perform(post("/api/v1/bookings/" + bookingId + "/cancellation")
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cancelled").value(true));

        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + otherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Canopus","start":"2026-10-31T10:30:00","end":"2026-10-31T11:30:00"}
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/bookings").header("Authorization", "Bearer " + otherToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == %d)]".formatted(bookingId)).isEmpty());

        mockMvc.perform(get("/api/v1/bookings").header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == %d && @.cancelled == true)]".formatted(bookingId)).exists());
    }

    @Test
    void rejectsSecondCancel() throws Exception {
        BookingHttpTest.CLOCK.setInstant(Instant.parse("2026-10-31T10:00:00+07:00"));
        String ownerToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "CancelTwice", "password1");

        MvcResult created = mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Vega","start":"2026-10-31T13:00:00","end":"2026-10-31T14:00:00"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        long bookingId = JsonPath.read(created.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(post("/api/v1/bookings/" + bookingId + "/cancellation")
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/bookings/" + bookingId + "/cancellation")
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsForeignCancelOnActiveBooking() throws Exception {
        BookingHttpTest.CLOCK.setInstant(Instant.parse("2026-10-31T10:00:00+07:00"));
        String ownerToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "ForeignCancel", "password1");
        String otherToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "ForeignIntruder", "password1");

        MvcResult created = mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Polaris","start":"2026-10-31T15:00:00","end":"2026-10-31T16:00:00"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        long bookingId = JsonPath.read(created.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(post("/api/v1/bookings/" + bookingId + "/cancellation")
                        .header("Authorization", "Bearer " + otherToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void ownerCanRebookCancelledStretch() throws Exception {
        BookingHttpTest.CLOCK.setInstant(Instant.parse("2026-10-31T10:00:00+07:00"));
        String ownerToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "RebookOwner", "password1");

        MvcResult created = mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Sirius","start":"2026-10-31T16:00:00","end":"2026-10-31T17:00:00"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        long bookingId = JsonPath.read(created.getResponse().getContentAsString(), "$.id");

        BookingHttpTest.CLOCK.setInstant(Instant.parse("2026-10-31T16:00:00+07:00"));

        mockMvc.perform(post("/api/v1/bookings/" + bookingId + "/cancellation")
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Sirius","start":"2026-10-31T16:00:00","end":"2026-10-31T17:00:00"}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void rejectsCancelAfterEnd() throws Exception {
        BookingHttpTest.CLOCK.setInstant(Instant.parse("2026-10-31T10:00:00+07:00"));
        String ownerToken = MeetingRoomsHttpSupport.registerAndLogin(mockMvc, "CancelEnded", "password1");

        MvcResult created = mockMvc.perform(post("/api/v1/bookings")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"room":"Rigel","start":"2026-10-31T10:00:00","end":"2026-10-31T11:00:00"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        long bookingId = JsonPath.read(created.getResponse().getContentAsString(), "$.id");

        BookingHttpTest.CLOCK.setInstant(Instant.parse("2026-10-31T11:00:00+07:00"));

        mockMvc.perform(post("/api/v1/bookings/" + bookingId + "/cancellation")
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isForbidden());
    }
}
