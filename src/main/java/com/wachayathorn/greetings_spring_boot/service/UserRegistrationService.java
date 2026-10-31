package com.wachayathorn.greetings_spring_boot.service;

import com.wachayathorn.greetings_spring_boot.dto.request.RegisterUserRequest;
import com.wachayathorn.greetings_spring_boot.dto.response.RegisterUserResponse;

public interface UserRegistrationService {

    RegisterUserResponse register(RegisterUserRequest request);
}
