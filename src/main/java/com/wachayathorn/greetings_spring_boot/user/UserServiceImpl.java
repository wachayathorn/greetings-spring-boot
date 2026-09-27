package com.wachayathorn.greetings_spring_boot.user;

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
        var userEntity = UserEntity.from(createUserReq);
        var savedUserEntity = userRepository.save(userEntity);
        return CreateUserRes.from(savedUserEntity);
    }   
}
