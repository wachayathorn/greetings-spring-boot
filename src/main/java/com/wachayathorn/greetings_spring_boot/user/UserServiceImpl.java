package com.wachayathorn.greetings_spring_boot.user;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.wachayathorn.greetings_spring_boot.user.dto.request.CreateUserReq;
import com.wachayathorn.greetings_spring_boot.user.dto.response.CreateUserRes;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public CreateUserRes createUser(CreateUserReq createUserReq) {
        var userEntity = new UserEntity(
                UUID.randomUUID().toString(),
                createUserReq.name(),
                createUserReq.email(),
                createUserReq.password());
        var savedUserEntity = userRepository.save(userEntity);
        return CreateUserRes.fromEntity(savedUserEntity);
    }
}
