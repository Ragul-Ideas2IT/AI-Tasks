package com.connectinghands.service;

import com.connectinghands.dto.AuthResponse;
import com.connectinghands.dto.LoginRequest;
import com.connectinghands.dto.RegisterRequest;
import com.connectinghands.dto.UpdateProfileRequest;
import com.connectinghands.dto.UserProfileDto;
import com.connectinghands.entity.User;

public interface UserService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    User getCurrentUser();
    void verifyEmail(String token);
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
    UserProfileDto getCurrentUserProfile();
    UserProfileDto updateProfile(UpdateProfileRequest request);
} 