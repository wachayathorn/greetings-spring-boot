package com.wachayathorn.greetings_spring_boot.user.service;

import org.springframework.stereotype.Service;

import com.wachayathorn.greetings_spring_boot.user.UserEntity;
import com.wachayathorn.greetings_spring_boot.user.dto.request.CreateUserReq;
import com.wachayathorn.greetings_spring_boot.user.dto.response.CreateUserRes;
import com.wachayathorn.greetings_spring_boot.user.repository.UserRepositoryImpl;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepositoryImpl userRepository;

    public UserServiceImpl(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CreateUserRes createUser(CreateUserReq createUserReq) {
        var userEntity = new UserEntity(
                null,
                createUserReq.name(),
                createUserReq.email(),
                createUserReq.password());
        var savedUserEntity = userRepository.save(userEntity);
        return CreateUserRes.fromEntity(savedUserEntity);
    }
}
