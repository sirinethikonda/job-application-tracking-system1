package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Requirement 6: Stateless API security
            .authorizeHttpRequests(auth -> auth
                // 1. Public Endpoints
                .requestMatchers("/auth/**", "/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/companies/**").permitAll()

                // 2. Requirement 4: Role-Based Access Control (RBAC)
                // Jobs: Only Recruiter creates; Anyone authenticated can view
                .requestMatchers(HttpMethod.POST, "/api/jobs/**").hasAuthority("RECRUITER")
                .requestMatchers(HttpMethod.GET, "/api/jobs/**").authenticated()

                // 3. Requirement 5 & 1: Applications & State Machine
                // FIX: Explicitly allow CANDIDATE to apply (POST)
                .requestMatchers(HttpMethod.POST, "/api/applications/**").hasAuthority("CANDIDATE")
                
                // FIX: Explicitly allow RECRUITER to change stages (PATCH)
                .requestMatchers(HttpMethod.PATCH, "/api/applications/**").hasAuthority("RECRUITER") 
                // Allow users to view their own applications
                .requestMatchers(HttpMethod.GET, "/api/applications/**").authenticated()
                
                // 4. Final Fallback
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}