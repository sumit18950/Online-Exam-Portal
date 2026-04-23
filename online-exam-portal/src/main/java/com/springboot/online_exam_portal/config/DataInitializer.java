package com.springboot.online_exam_portal.config;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin.default.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@gmail.com").isPresent()) {
                return;
            }

            Role adminRole = roleRepository.findByRoleName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("ADMIN role not found in DB"));

            User admin = new User();
            admin.setUsername("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPasswordHash(passwordEncoder.encode(adminPassword));
            admin.setRole(adminRole);

            userRepository.save(admin);
            System.out.println("Default admin created!");
        };
    }
}