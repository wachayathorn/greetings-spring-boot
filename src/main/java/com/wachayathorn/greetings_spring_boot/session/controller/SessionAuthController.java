package com.wachayathorn.greetings_spring_boot.session.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.wachayathorn.greetings_spring_boot.session.dto.request.LoginRequest;
import com.wachayathorn.greetings_spring_boot.session.dto.response.LoginResponse;
import com.wachayathorn.greetings_spring_boot.session.service.SessionAuthService;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionAuthController {

    private final SessionAuthService sessionAuthService;

    public SessionAuthController(SessionAuthService sessionAuthService) {
        this.sessionAuthService = sessionAuthService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = sessionAuthService.login(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        sessionAuthService.logout(authorization);
        return ResponseEntity.noContent().build();
    }
}
