package com.wachayathorn.greetings.greeting;

import com.wachayathorn.greetings.greeting.dto.CreateGreetingRequest;
import com.wachayathorn.greetings.greeting.dto.GreetingResponse;
import com.wachayathorn.greetings.greeting.dto.UpdateGreetingRequest;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GreetingService {

    private final GreetingRepository greetingRepository;

    public GreetingService(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    @Transactional
    public GreetingResponse create(CreateGreetingRequest request) {
        GreetingEntity saved = greetingRepository.save(new GreetingEntity(request.name()));
        return toResponse(saved);
    }

    public List<GreetingResponse> findAll() {
        return greetingRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public GreetingResponse findById(Long id) {
        return toResponse(getEntity(id));
    }

    @Transactional
    public GreetingResponse update(Long id, UpdateGreetingRequest request) {
        GreetingEntity entity = getEntity(id);
        entity.setName(request.name());
        return toResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        Long greetingId = Objects.requireNonNull(id, "id must not be null");
        if (!greetingRepository.existsById(greetingId)) {
            throw new GreetingNotFoundException(greetingId);
        }
        greetingRepository.deleteById(greetingId);
    }

    private GreetingEntity getEntity(Long id) {
        Long greetingId = Objects.requireNonNull(id, "id must not be null");
        return greetingRepository.findById(greetingId)
                .orElseThrow(() -> new GreetingNotFoundException(greetingId));
    }

    private GreetingResponse toResponse(GreetingEntity entity) {
        return new GreetingResponse(entity.getId(), entity.getName(), entity.getCreatedAt());
    }
}
