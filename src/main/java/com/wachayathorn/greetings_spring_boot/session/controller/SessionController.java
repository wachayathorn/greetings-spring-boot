package com.wachayathorn.greetings_spring_boot.session.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wachayathorn.greetings_spring_boot.session.dto.request.LoginRequest;
import com.wachayathorn.greetings_spring_boot.session.dto.response.LoginResponse;
import com.wachayathorn.greetings_spring_boot.session.service.SessionService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    @Operation(security = {})
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = sessionService.login(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        sessionService.logout(authorization);
        return ResponseEntity.noContent().build();
    }
}
