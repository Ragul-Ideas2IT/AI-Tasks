package com.collabdocs.user.controller;

import com.collabdocs.user.model.User;
import com.collabdocs.user.model.UserRegistrationDTO;
import com.collabdocs.user.model.UserLoginDTO;
import com.collabdocs.user.service.UserService;
import com.collabdocs.user.config.JwtProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        User user = userService.registerUser(registrationDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        User user;
        if (loginDTO.getUsernameOrEmail().contains("@")) {
            user = userService.findByEmail(loginDTO.getUsernameOrEmail());
        } else {
            user = userService.findByUsername(loginDTO.getUsernameOrEmail());
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtProvider.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
} 