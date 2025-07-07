package com.collabdocs.user.service;

import com.collabdocs.user.model.User;
import com.collabdocs.user.model.UserRegistrationDTO;

public interface UserService {
    User registerUser(UserRegistrationDTO registrationDTO);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User updateUser(Long userId, UserRegistrationDTO userDTO);
    void deleteUser(Long userId);
} 