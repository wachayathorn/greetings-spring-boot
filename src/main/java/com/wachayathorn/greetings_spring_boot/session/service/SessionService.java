package com.wachayathorn.greetings_spring_boot.session.service;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wachayathorn.greetings_spring_boot.session.config.AuthProperties;
import com.wachayathorn.greetings_spring_boot.session.dto.request.LoginRequest;
import com.wachayathorn.greetings_spring_boot.session.dto.response.LoginResponse;
import com.wachayathorn.greetings_spring_boot.session.entity.SessionEntity;
import com.wachayathorn.greetings_spring_boot.session.exception.InvalidCredentialsException;
import com.wachayathorn.greetings_spring_boot.session.exception.InvalidSessionTokenException;
import com.wachayathorn.greetings_spring_boot.session.exception.SessionAlreadyLoggedOutException;
import com.wachayathorn.greetings_spring_boot.session.repository.SessionRepository;
import com.wachayathorn.greetings_spring_boot.session.security.AuthenticatedUser;
import com.wachayathorn.greetings_spring_boot.user.entity.UserEntity;
import com.wachayathorn.greetings_spring_boot.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class SessionService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthProperties authProperties;
    private final Clock clock;
    private final SecretKey signingKey;

    public SessionService(
            UserRepository userRepository,
            SessionRepository sessionRepository,
            PasswordEncoder passwordEncoder,
            AuthProperties authProperties,
            Clock clock) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.authProperties = authProperties;
        this.clock = clock;
        this.signingKey = Keys.hmacShaKeyFor(authProperties.jwtSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        UserEntity user = userRepository
                .findByUsernameNormalized(request.username().toLowerCase())
                .filter(found -> passwordEncoder.matches(request.password(), found.getPasswordHash()))
                .orElseThrow(InvalidCredentialsException::new);

        Instant expiresAt = clock.instant().plus(authProperties.sessionDuration());
        SessionEntity session = new SessionEntity(UUID.randomUUID(), user, expiresAt);
        sessionRepository.save(session);
        return new LoginResponse(createToken(session));
    }

    @Transactional
    public void logout(String bearerToken) {
        UUID sessionId = parseSessionId(extractRawToken(bearerToken));
        if (!sessionRepository.existsById(sessionId)) {
            throw new SessionAlreadyLoggedOutException();
        }
        sessionRepository.deleteById(sessionId);
    }

    @Transactional(readOnly = true)
    public AuthenticatedUser authenticate(String bearerToken) {
        UUID sessionId = parseSessionId(extractRawToken(bearerToken));
        SessionEntity session = sessionRepository
                .findById(sessionId)
                .orElseThrow(InvalidSessionTokenException::new);

        if (session.getExpiresAt().isBefore(clock.instant())) {
            throw new InvalidSessionTokenException();
        }

        return AuthenticatedUser.from(session.getUser());
    }

    private String createToken(SessionEntity session) {
        return Jwts.builder()
                .subject(session.getId().toString())
                .issuedAt(Date.from(clock.instant()))
                .expiration(Date.from(session.getExpiresAt()))
                .signWith(signingKey)
                .compact();
    }

    private UUID parseSessionId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return UUID.fromString(claims.getSubject());
        } catch (JwtException | IllegalArgumentException exception) {
            throw new InvalidSessionTokenException();
        }
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
