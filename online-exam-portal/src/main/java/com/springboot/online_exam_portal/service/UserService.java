package com.springboot.online_exam_portal.service;

import com.springboot.online_exam_portal.dto.AdminCreateUserRequest;
import com.springboot.online_exam_portal.dto.RegisterRequest;
import com.springboot.online_exam_portal.entity.Role;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.repository.RoleRepository;
import com.springboot.online_exam_portal.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class UserService {

    private static final String DEFAULT_REGISTRATION_ROLE = "ROLE_STUDENT";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder){

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        Role role = findRoleOrThrow(DEFAULT_REGISTRATION_ROLE);
        User user = buildUser(request.getUsername(), request.getEmail(), request.getPassword(), role);
        return userRepository.save(user);
    }

    public User createUserByAdmin(AdminCreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        if (request.getRole() == null || request.getRole().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required");
        }

        Role role = findRoleOrThrow(request.getRole());
        User user = buildUser(request.getUsername(), request.getEmail(), request.getPassword(), role);
        return userRepository.save(user);
    }

    public User login(String email,String password){

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if(!passwordEncoder.matches(password,user.getPasswordHash())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return user;
    }

    private User buildUser(String username, String email, String rawPassword, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    private Role findRoleOrThrow(String roleInput) {
        String normalizedRole = normalizeRoleName(roleInput);
        return roleRepository.findByRoleName(normalizedRole)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found: " + normalizedRole));
    }

    private String normalizeRoleName(String roleInput) {
        String normalized = roleInput.trim().toUpperCase(Locale.ROOT);
        if (normalized.startsWith("ROLE_")) {
            normalized = normalized.substring("ROLE_".length());
        }
        return normalized;
    }
}