package com.wachayathorn.greetings_spring_boot.user.service;

import com.wachayathorn.greetings_spring_boot.user.dto.request.RegisterUserRequest;
import com.wachayathorn.greetings_spring_boot.user.dto.response.RegisterUserResponse;

public interface UserRegistrationService {
    RegisterUserResponse register(RegisterUserRequest request);
}
