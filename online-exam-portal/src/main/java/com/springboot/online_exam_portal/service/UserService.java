package com.springboot.online_exam_portal.service;

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
import java.util.Set;

@Service
public class UserService {

    private static final String DEFAULT_REGISTRATION_ROLE = "STUDENT";
    private static final Set<String> ALLOWED_REGISTRATION_ROLES = Set.of("ADMIN", "TEACHER", "STUDENT");

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

        if(userRepository.existsByEmail(request.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
        }

        String requestedRole = request.getRole() == null
                ? DEFAULT_REGISTRATION_ROLE
                : request.getRole().trim().toUpperCase();

        if (!ALLOWED_REGISTRATION_ROLES.contains(requestedRole)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role: " + requestedRole);
        }

        Role role = roleRepository
                .findByRoleName(requestedRole)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found: " + requestedRole));

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User login(String email,String password){

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(password,user.getPasswordHash())){
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}