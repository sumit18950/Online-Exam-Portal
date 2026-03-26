package com.springboot.online_exam_portal.config;

import com.springboot.online_exam_portal.security.JwtFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security Configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //  Disable CSRF (for APIs)
                .csrf(csrf -> csrf.disable())

                // git No session (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                //  Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC APIs
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // SELF-SERVICE (authenticated users)
                        .requestMatchers(HttpMethod.GET, "/api/users/profile").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/update-profile").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/users/change-password").authenticated()

                        // ADMIN ONLY
                        .requestMatchers(HttpMethod.GET, "/api/users/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")

                        // ADMIN + TEACHER (teacher access is constrained in controller to STUDENT users only)
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/users/by-id/{id}").hasAnyRole("ADMIN", "TEACHER")

                        .requestMatchers("/api/questions/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/exams/attempt").hasRole("STUDENT")

                        // ALL other APIs require login
                        .anyRequest().authenticated()
                )

                //  Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}