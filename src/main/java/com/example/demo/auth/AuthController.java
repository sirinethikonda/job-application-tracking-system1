package com.example.demo.auth;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String fullName = body.get("fullName");
        String role = body.get("role");
        String companyId = body.get("companyId");

        if (email == null || password == null || role == null) return ResponseEntity.badRequest().body("Missing fields");

        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) return ResponseEntity.badRequest().body("Email already exists");

        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setEmail(email);
        u.setPasswordHash(passwordEncoder.encode(password));
        u.setFullName(fullName);
        u.setRole(role);
        u.setCompanyId(companyId);
        u.setCreatedAt(Timestamp.from(Instant.now()));
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("id", u.getId(), "email", u.getEmail(), "role", u.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if (email == null || password == null) return ResponseEntity.badRequest().body("Missing");

        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) return ResponseEntity.status(401).body("Invalid credentials");
        User u = opt.get();
        if (!passwordEncoder.matches(password, u.getPasswordHash())) return ResponseEntity.status(401).body("Invalid credentials");
        String token = jwtUtils.generateToken(u);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
