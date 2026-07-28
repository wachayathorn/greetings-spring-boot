package com.wachayathorn.greetings.greeting;

import com.wachayathorn.greetings.greeting.dto.CreateGreetingRequest;
import com.wachayathorn.greetings.greeting.dto.GreetingResponse;
import com.wachayathorn.greetings.greeting.dto.UpdateGreetingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/greetings")
@Tag(name = "Greetings")
public class GreetingController {

    private final GreetingService greetingService;

    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create greeting")
    public GreetingResponse create(@Valid @RequestBody CreateGreetingRequest request) {
        return greetingService.create(request);
    }

    @GetMapping
    @Operation(summary = "List greetings")
    public List<GreetingResponse> findAll() {
        return greetingService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get greeting by id")
    public GreetingResponse findById(@PathVariable Long id) {
        return greetingService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update greeting")
    public GreetingResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGreetingRequest request
    ) {
        return greetingService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete greeting")
    public void delete(@PathVariable Long id) {
        greetingService.delete(id);
    }
}
