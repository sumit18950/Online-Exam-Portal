package com.springboot.online_exam_portal.controller;

import com.springboot.online_exam_portal.dto.LoginRequest;
import com.springboot.online_exam_portal.dto.RegisterRequest;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.security.JwtUtil;
import com.springboot.online_exam_portal.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil){
        this.userService=userService;
        this.jwtUtil=jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request){

        return userService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){

        User user = userService.login(request.getEmail(),request.getPassword());

        return jwtUtil.generateToken(user.getEmail());
    }
}