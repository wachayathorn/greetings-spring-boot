package com.wachayathorn.greetings_spring_boot.session.service;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import com.wachayathorn.greetings_spring_boot.session.config.AuthProperties;
import com.wachayathorn.greetings_spring_boot.session.exception.InvalidSessionTokenException;
import com.wachayathorn.greetings_spring_boot.session.entity.SessionEntity;

@Service
public class JwtSessionTokenServiceImpl implements JwtSessionTokenService {

    private final SecretKey signingKey;
    private final Clock clock;

    public JwtSessionTokenServiceImpl(AuthProperties authProperties, Clock clock) {
        byte[] keyBytes = authProperties.jwtSecret().getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.clock = clock;
    }

    @Override
    public String createToken(SessionEntity session) {
        Instant expiresAt = session.getExpiresAt();
        return Jwts.builder()
                .subject(session.getId().toString())
                .issuedAt(Date.from(clock.instant()))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public UUID parseSessionId(String token) {
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
}
