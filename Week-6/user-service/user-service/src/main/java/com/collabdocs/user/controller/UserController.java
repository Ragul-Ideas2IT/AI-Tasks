package com.collabdocs.user.controller;

import com.collabdocs.user.model.User;
import com.collabdocs.user.model.UserRegistrationDTO;
import com.collabdocs.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateCurrentUser(@AuthenticationPrincipal User user,
                                                  @Valid @RequestBody UserRegistrationDTO userDTO) {
        User updated = userService.updateUser(user.getId(), userDTO);
        return ResponseEntity.ok(updated);
    }
} 