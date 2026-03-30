package com.springboot.online_exam_portal.config;

import com.springboot.online_exam_portal.security.JwtFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

    // Allow browser preflight + frontend origins for API calls
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Security Configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                //  Disable CSRF (for APIs)
                .csrf(csrf -> csrf.disable())

                // Enable CORS handling via CorsConfigurationSource bean
                .cors(Customizer.withDefaults())

                // git No session (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                //  Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // Let browser preflight pass before auth checks
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // PUBLIC APIs
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()

                        // SELF-SERVICE (authenticated users)
                        .requestMatchers(HttpMethod.GET, "/api/users/profile").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/update-profile").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/users/change-password").authenticated()

                        // ADMIN ONLY
                        .requestMatchers(HttpMethod.GET, "/api/users/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")

                        // ADMIN + TEACHER (teacher access is constrained in controller to STUDENT users only)
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/users/by-id/{id}").hasAnyRole("ADMIN", "TEACHER")

                        .requestMatchers("/api/questions/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/api/options/**").hasAnyRole("ADMIN", "TEACHER")

                        // Exam & Subject APIs: Protected - requires authentication, detailed role checks in controller
                        .requestMatchers("/api/exams/subjects").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/exams/subjects").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/api/exams/subjects/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/exams/subjects/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/exams/subjects/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/exams").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.POST, "/api/exams/admin/create").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/exams/teacher/create").hasRole("TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/api/exams/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.PATCH, "/api/exams/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.DELETE, "/api/exams/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/api/exams/**").authenticated()


                        // Result Analysis Authorization
                        .requestMatchers(HttpMethod.GET, "/results").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/results/*").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/results/user/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/results/exam/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/results/leaderboard/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/results/review").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/results/review-history/**").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.GET, "/results/certificate/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/results/export/**").hasAnyRole("ADMIN", "TEACHER")

                        // ALL other APIs require login
                        .anyRequest().authenticated()
                )

                //  Add JWT filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}