package com.wachayathorn.greetings.greeting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wachayathorn.greetings.greeting.dto.CreateGreetingRequest;
import com.wachayathorn.greetings.greeting.dto.GreetingResponse;
import com.wachayathorn.greetings.greeting.dto.UpdateGreetingRequest;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GreetingServiceTest {

    @Mock
    private GreetingRepository greetingRepository;

    @InjectMocks
    private GreetingService greetingService;

    @Test
    void create_shouldSaveAndReturnResponse() {
        when(greetingRepository.save(any(GreetingEntity.class))).thenAnswer(invocation -> {
            GreetingEntity entity = invocation.getArgument(0);
            // simulate DB-generated id + @PrePersist
            GreetingEntity saved = new GreetingEntity(entity.getName());
            setIdAndCreatedAt(saved, 1L, Instant.parse("2026-07-28T00:00:00Z"));
            return saved;
        });

        GreetingResponse response = greetingService.create(new CreateGreetingRequest("Alice"));

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Alice");
        assertThat(response.createdAt()).isEqualTo(Instant.parse("2026-07-28T00:00:00Z"));

        ArgumentCaptor<GreetingEntity> captor = ArgumentCaptor.forClass(GreetingEntity.class);
        verify(greetingRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Alice");
    }

    @Test
    void findAll_shouldReturnMappedList() {
        GreetingEntity entity = new GreetingEntity("Bob");
        setIdAndCreatedAt(entity, 2L, Instant.parse("2026-07-28T01:00:00Z"));
        when(greetingRepository.findAll()).thenReturn(List.of(entity));

        List<GreetingResponse> result = greetingService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Bob");
    }

    @Test
    void findById_shouldThrowWhenMissing() {
        when(greetingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> greetingService.findById(99L))
                .isInstanceOf(GreetingNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void update_shouldChangeName() {
        GreetingEntity entity = new GreetingEntity("Old");
        setIdAndCreatedAt(entity, 3L, Instant.parse("2026-07-28T02:00:00Z"));
        when(greetingRepository.findById(3L)).thenReturn(Optional.of(entity));

        GreetingResponse response = greetingService.update(3L, new UpdateGreetingRequest("New"));

        assertThat(response.name()).isEqualTo("New");
        assertThat(entity.getName()).isEqualTo("New");
    }

    @Test
    void delete_shouldThrowWhenMissing() {
        when(greetingRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> greetingService.delete(99L))
                .isInstanceOf(GreetingNotFoundException.class);

        verify(greetingRepository, never()).deleteById(99L);
    }

    @Test
    void delete_shouldCallRepositoryWhenExists() {
        when(greetingRepository.existsById(1L)).thenReturn(true);

        greetingService.delete(1L);

        verify(greetingRepository).deleteById(1L);
    }

    private static void setIdAndCreatedAt(GreetingEntity entity, Long id, Instant createdAt) {
        try {
            var idField = GreetingEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);

            var createdAtField = GreetingEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(entity, createdAt);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
