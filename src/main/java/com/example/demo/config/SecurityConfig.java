package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for showcase application
 * Permits all requests - FOR DEMONSTRATION ONLY
 *
 * In production, configure proper authentication and authorization
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // Allow all requests for showcase
            )
            .csrf(csrf -> csrf.disable())  // Disable CSRF for showcase
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));  // Allow H2 console

        return http.build();
    }
}
