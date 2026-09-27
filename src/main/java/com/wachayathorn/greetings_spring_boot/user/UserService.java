package com.wachayathorn.greetings_spring_boot.user;

import com.wachayathorn.greetings_spring_boot.user.dto.request.CreateUserReq;
import com.wachayathorn.greetings_spring_boot.user.dto.response.CreateUserRes;

public interface UserService {
    CreateUserRes createUser(CreateUserReq createUserDto);
}
