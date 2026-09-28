package com.wachayathorn.greetings_spring_boot.user.service;

import com.wachayathorn.greetings_spring_boot.user.dto.request.CreateUserReq;
import com.wachayathorn.greetings_spring_boot.user.dto.response.CreateUserRes;

public interface UserService {
    CreateUserRes createUser(CreateUserReq createUserReq);
}
