package com.wachayathorn.greetings.greeting;

import com.wachayathorn.greetings.greeting.dto.CreateGreetingRequest;
import com.wachayathorn.greetings.greeting.dto.GreetingResponse;
import com.wachayathorn.greetings.greeting.dto.UpdateGreetingRequest;
import java.util.List;
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
        if (!greetingRepository.existsById(id)) {
            throw new GreetingNotFoundException(id);
        }
        greetingRepository.deleteById(id);
    }

    private GreetingEntity getEntity(Long id) {
        return greetingRepository.findById(id)
                .orElseThrow(() -> new GreetingNotFoundException(id));
    }

    private GreetingResponse toResponse(GreetingEntity entity) {
        return new GreetingResponse(entity.getId(), entity.getName(), entity.getCreatedAt());
    }
}
