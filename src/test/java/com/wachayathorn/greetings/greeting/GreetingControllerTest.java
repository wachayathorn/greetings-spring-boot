package com.wachayathorn.greetings.greeting;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wachayathorn.greetings.common.GlobalExceptionHandler;
import com.wachayathorn.greetings.greeting.dto.CreateGreetingRequest;
import com.wachayathorn.greetings.greeting.dto.GreetingResponse;
import com.wachayathorn.greetings.greeting.dto.UpdateGreetingRequest;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GreetingController.class)
@Import(GlobalExceptionHandler.class)
class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GreetingService greetingService;

    @Test
    void create_shouldReturn201() throws Exception {
        when(greetingService.create(any(CreateGreetingRequest.class)))
                .thenReturn(new GreetingResponse(1L, "Alice", Instant.parse("2026-07-28T00:00:00Z")));

        mockMvc.perform(post("/api/v1/greetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CreateGreetingRequest("Alice"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void create_shouldReturn400WhenNameBlank() throws Exception {
        mockMvc.perform(post("/api/v1/greetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_shouldReturn200() throws Exception {
        when(greetingService.findAll()).thenReturn(List.of(
                new GreetingResponse(1L, "Alice", Instant.parse("2026-07-28T00:00:00Z"))
        ));

        mockMvc.perform(get("/api/v1/greetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void findById_shouldReturn404WhenMissing() throws Exception {
        when(greetingService.findById(99L)).thenThrow(new GreetingNotFoundException(99L));

        mockMvc.perform(get("/api/v1/greetings/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        when(greetingService.update(eq(1L), any(UpdateGreetingRequest.class)))
                .thenReturn(new GreetingResponse(1L, "Updated", Instant.parse("2026-07-28T00:00:00Z")));

        mockMvc.perform(put("/api/v1/greetings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateGreetingRequest("Updated"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        doNothing().when(greetingService).delete(1L);

        mockMvc.perform(delete("/api/v1/greetings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenMissing() throws Exception {
        doThrow(new GreetingNotFoundException(99L)).when(greetingService).delete(99L);

        mockMvc.perform(delete("/api/v1/greetings/99"))
                .andExpect(status().isNotFound());
    }
}
