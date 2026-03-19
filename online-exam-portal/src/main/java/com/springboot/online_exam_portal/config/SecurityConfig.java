package com.springboot.online_exam_portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * SecurityConfig - Configure Spring Security for the Exam Management System
 * 
 * COMMENT: This configuration:
 * - Disables CSRF protection for API endpoints (needed for POST/PUT/DELETE)
 * - Allows all /api/exams endpoints without authentication
 * - Enables CORS for cross-origin requests
 * - Uses httpBasic() for optional authentication
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * COMMENT: Configure HTTP Security
     * - Disable CSRF for stateless APIs
     * - Permit all requests to /api/exams endpoints
     * - Enable CORS
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // COMMENT: Disable CSRF protection for APIs
            .csrf(csrf -> csrf.disable())
            
            // COMMENT: Configure authorization for endpoints
            .authorizeHttpRequests(authz -> authz
                // COMMENT: Allow all requests to /api/exams endpoints (PUBLIC)
                .requestMatchers("/api/exams/**").permitAll()
                // COMMENT: Allow all other requests
                .anyRequest().permitAll()
            )
            
            // COMMENT: Enable CORS configuration
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // COMMENT: Enable HTTP Basic authentication (optional)
            .httpBasic(basic -> basic.disable());
        
        return http.build();
    }

    /**
     * COMMENT: Configure CORS for cross-origin requests
     * - Allow requests from any origin
     * - Allow GET, POST, PUT, DELETE methods
     * - Allow common headers
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // COMMENT: Allow requests from any origin
        configuration.setAllowedOrigins(Arrays.asList("*"));
        
        // COMMENT: Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // COMMENT: Allow common headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Content-Type", 
            "Authorization", 
            "userId",
            "X-Requested-With",
            "Accept"
        ));
        
        // COMMENT: Allow credentials in requests
        configuration.setAllowCredentials(false);
        
        // COMMENT: Set max age for preflight requests (1 hour)
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

