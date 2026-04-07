package com.springboot.online_exam_portal.controller;

import com.springboot.online_exam_portal.dto.AdminCreateUserRequest;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody AdminCreateUserRequest request) {
        return userService.createUserByAdmin(request);
    }
}

