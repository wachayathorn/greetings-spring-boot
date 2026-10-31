package com.wachayathorn.greetings_spring_boot.session.service;

import java.util.UUID;

import com.wachayathorn.greetings_spring_boot.session.entity.SessionEntity;

public interface JwtSessionTokenService {

    String createToken(SessionEntity session);

    UUID parseSessionId(String token);
}
