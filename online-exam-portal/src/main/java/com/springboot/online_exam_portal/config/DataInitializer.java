package com.springboot.online_exam_portal.config;

import com.springboot.online_exam_portal.entity.Role;
import com.springboot.online_exam_portal.entity.User;
import com.springboot.online_exam_portal.repository.RoleRepository;
import com.springboot.online_exam_portal.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {

            // Check if admin already exists
            if (userRepository.findByEmail("admin@gmail.com").isPresent()) {
                return;
            }

            // Resolve ROLE_ADMIN once and fail fast if seed data is missing.
            Role adminRole = roleRepository.findByRoleName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("ADMIN role not found in DB"));

            // Create admin user
            User admin = new User();
            admin.setUsername("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);

            userRepository.save(admin);

            System.out.println("Default admin created!");
        };
    }
}