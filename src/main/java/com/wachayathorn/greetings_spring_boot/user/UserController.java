package com.wachayathorn.greetings_spring_boot.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wachayathorn.greetings_spring_boot.user.dto.request.CreateUserReq;
import com.wachayathorn.greetings_spring_boot.user.dto.response.CreateUserRes;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public CreateUserRes createUser(@Valid @RequestBody CreateUserReq createUserReq) {
        return userService.createUser(createUserReq);
    }
}
