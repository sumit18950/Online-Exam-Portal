package com.springboot.online_exam_portal.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.springboot.online_exam_portal.dto.GoogleLoginRequest;
import com.springboot.online_exam_portal.dto.LoginRequest;
import com.springboot.online_exam_portal.dto.RegisterRequest;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.security.JwtUtil;
import com.springboot.online_exam_portal.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final GoogleIdTokenVerifier googleVerifier;

    public AuthController(UserService userService,
                          JwtUtil jwtUtil,
                          @Value("${google.client.id}") String googleClientId) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.googleVerifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getEmail(), request.getPassword());
        return jwtUtil.generateToken(user.getEmail());
    }

    @PostMapping("/google")
    public String googleLogin(@RequestBody GoogleLoginRequest request) {
        try {
            GoogleIdToken idToken = googleVerifier.verify(request.getCredential());
            if (idToken == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            User user = userService.findOrCreateGoogleUser(email, name);
            return jwtUtil.generateToken(user.getEmail());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google authentication failed");
        }
    }
}
