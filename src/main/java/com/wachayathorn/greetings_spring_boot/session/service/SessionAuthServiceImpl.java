package com.wachayathorn.greetings_spring_boot.session.service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wachayathorn.greetings_spring_boot.session.config.AuthProperties;
import com.wachayathorn.greetings_spring_boot.session.security.AuthenticatedUser;
import com.wachayathorn.greetings_spring_boot.session.exception.InvalidCredentialsException;
import com.wachayathorn.greetings_spring_boot.session.exception.InvalidSessionTokenException;
import com.wachayathorn.greetings_spring_boot.session.dto.request.LoginRequest;
import com.wachayathorn.greetings_spring_boot.session.dto.response.LoginResponse;
import com.wachayathorn.greetings_spring_boot.session.exception.SessionAlreadyLoggedOutException;
import com.wachayathorn.greetings_spring_boot.session.entity.SessionEntity;
import com.wachayathorn.greetings_spring_boot.session.repository.SessionRepository;
import com.wachayathorn.greetings_spring_boot.user.entity.UserEntity;
import com.wachayathorn.greetings_spring_boot.user.repository.UserRepository;

@Service
public class SessionAuthServiceImpl implements SessionAuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtSessionTokenService jwtSessionTokenService;
    private final AuthProperties authProperties;
    private final Clock clock;

    public SessionAuthServiceImpl(
            UserRepository userRepository,
            SessionRepository sessionRepository,
            PasswordEncoder passwordEncoder,
            JwtSessionTokenService jwtSessionTokenService,
            AuthProperties authProperties,
            Clock clock) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSessionTokenService = jwtSessionTokenService;
        this.authProperties = authProperties;
        this.clock = clock;
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        UserEntity user = userRepository
                .findByUsernameNormalized(request.username().toLowerCase())
                .filter(found -> passwordEncoder.matches(request.password(), found.getPasswordHash()))
                .orElseThrow(InvalidCredentialsException::new);

        Instant expiresAt = clock.instant().plus(authProperties.sessionDuration());
        SessionEntity session = new SessionEntity(UUID.randomUUID(), user, expiresAt);
        sessionRepository.save(session);
        String token = jwtSessionTokenService.createToken(session);
        return new LoginResponse(token);
    }

    @Override
    @Transactional
    public void logout(String bearerToken) {
        UUID sessionId = jwtSessionTokenService.parseSessionId(extractRawToken(bearerToken));
        if (!sessionRepository.existsById(sessionId)) {
            throw new SessionAlreadyLoggedOutException();
        }
        sessionRepository.deleteById(sessionId);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticatedUser authenticate(String bearerToken) {
        UUID sessionId = jwtSessionTokenService.parseSessionId(extractRawToken(bearerToken));
        SessionEntity session = sessionRepository
                .findById(sessionId)
                .orElseThrow(InvalidSessionTokenException::new);

        if (session.getExpiresAt().isBefore(clock.instant())) {
            throw new InvalidSessionTokenException();
        }

        return AuthenticatedUser.from(session.getUser());
    }

    private static String extractRawToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new InvalidSessionTokenException();
        }
        if (!authorizationHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            throw new InvalidSessionTokenException();
        }
        String token = authorizationHeader.substring(7).trim();
        if (token.isEmpty()) {
            throw new InvalidSessionTokenException();
        }
        return token;
    }
}
