package com.connectinghands.service.impl;

import com.connectinghands.dto.*;
import com.connectinghands.entity.User;
import com.connectinghands.exception.ValidationException;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.security.jwt.JwtTokenProvider;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of the UserService interface.
 * Handles user management and authentication.
 *
 * @author Ragul Venkatesan
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email is already taken");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());
        user.setVerificationToken(UUID.randomUUID().toString());

        user = userRepository.save(user);
        auditLogService.logAction(user.getId(), "REGISTER", "User", user.getId(), null, null, null, null);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String token = tokenProvider.generateToken(authentication);
        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = (User) authentication.getPrincipal();
        String token = tokenProvider.generateToken(authentication);
        auditLogService.logAction(user.getId(), "LOGIN", "User", user.getId(), null, null, null, null);
        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new ValidationException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
        auditLogService.logAction(user.getId(), "VERIFY_EMAIL", "User", user.getId(), null, null, null, null);
    }

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("User not found with email: " + email));

        user.setResetToken(UUID.randomUUID().toString());
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        auditLogService.logAction(user.getId(), "REQUEST_PASSWORD_RESET", "User", user.getId(), null, null, null, null);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new ValidationException("Invalid reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        auditLogService.logAction(user.getId(), "RESET_PASSWORD", "User", user.getId(), null, null, null, null);
    }

    @Override
    public UserProfileDto getCurrentUserProfile() {
        User user = getCurrentUser();
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole().name());
        dto.setEmailVerified(user.isEmailVerified());
        return dto;
    }

    @Override
    @Transactional
    public UserProfileDto updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();
        String oldFirstName = user.getFirstName();
        String oldLastName = user.getLastName();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        userRepository.save(user);
        auditLogService.logAction(user.getId(), "UPDATE_PROFILE", "User", user.getId(),
                String.format("{\"firstName\":\"%s\",\"lastName\":\"%s\"}", oldFirstName, oldLastName),
                String.format("{\"firstName\":\"%s\",\"lastName\":\"%s\"}", user.getFirstName(), user.getLastName()),
                null, null);
        return getCurrentUserProfile();
    }
}
