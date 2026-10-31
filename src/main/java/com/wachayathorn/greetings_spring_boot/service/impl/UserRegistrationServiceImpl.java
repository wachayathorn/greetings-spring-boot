package com.wachayathorn.greetings_spring_boot.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wachayathorn.greetings_spring_boot.dto.request.RegisterUserRequest;
import com.wachayathorn.greetings_spring_boot.dto.response.RegisterUserResponse;
import com.wachayathorn.greetings_spring_boot.entity.UserEntity;
import com.wachayathorn.greetings_spring_boot.exception.DuplicateUsernameException;
import com.wachayathorn.greetings_spring_boot.repository.UserRepository;
import com.wachayathorn.greetings_spring_boot.service.UserRegistrationService;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {
        String normalized = request.username().toLowerCase();
        if (userRepository.existsByUsernameNormalized(normalized)) {
            throw new DuplicateUsernameException();
        }

        UserEntity user = new UserEntity(
                request.username(),
                normalized,
                passwordEncoder.encode(request.password()));
        userRepository.save(user);
        return new RegisterUserResponse(user.getUsername());
    }
}
