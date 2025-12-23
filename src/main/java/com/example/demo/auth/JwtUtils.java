package com.example.demo.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.example.demo.user.User;

@Component
public class JwtUtils {

    private final Key key;
    private final long expirationMs;

    // Requirement 6: Loading secrets from STS4 Environment tab
    public JwtUtils(@Value("${app.jwt.secret}") String secret, 
                    @Value("${app.jwt.expiration-ms}") long expirationMs) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters for security.");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    // Generates a token with Role and Email
    public String generateToken(User user) {
        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole()); // Critical for Requirement 4
        claims.put("userId", user.getId());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    // Validates the token structure
    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    // Helper to extract the Role specifically for SecurityConfig
    public String getRoleFromToken(String token) {
        return parse(token).getBody().get("role", String.class);
    }
}