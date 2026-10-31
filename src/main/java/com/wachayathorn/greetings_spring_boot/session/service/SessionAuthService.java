package com.wachayathorn.greetings_spring_boot.session.service;

import com.wachayathorn.greetings_spring_boot.session.security.AuthenticatedUser;
import com.wachayathorn.greetings_spring_boot.session.dto.request.LoginRequest;
import com.wachayathorn.greetings_spring_boot.session.dto.response.LoginResponse;

public interface SessionAuthService {

    LoginResponse login(LoginRequest request);

    void logout(String bearerToken);

    AuthenticatedUser authenticate(String bearerToken);
}
