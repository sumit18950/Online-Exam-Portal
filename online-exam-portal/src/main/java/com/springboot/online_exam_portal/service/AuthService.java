package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.dto.RegisterRequest;
import com.springboot.online_exam_portal.entity.Role;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.repository.RoleRepository;
import com.springboot.online_exam_portal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AuthService {

    private static final Set<String> ALLOWED_ROLES = Set.of("ADMIN", "TEACHER", "STUDENT");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String requestedRole = request.getRole() == null ? "STUDENT" : request.getRole().trim().toUpperCase();
        if (!ALLOWED_ROLES.contains(requestedRole)) {
            throw new RuntimeException("Invalid role: " + requestedRole);
        }

        Role role = roleRepository.findByRoleName(requestedRole)
                .orElseThrow(() -> new RuntimeException("Role not found in DB: " + requestedRole));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        return "User registered successfully";
    }
}
