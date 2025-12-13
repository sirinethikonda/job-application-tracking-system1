package com.example.demo.user;

import java.util.Optional;
import com.example.demo.dto.AuthRequestDto;

public interface AppUserDetailsService {
    User registerNewUser(AuthRequestDto request);
    Optional<User> findByEmail(String email);
}
