package com.connectinghands.controller;

import com.connectinghands.config.TestConfig;
import com.connectinghands.dto.UpdateProfileRequest;
import com.connectinghands.dto.UserProfileDto;
import com.connectinghands.dto.PasswordResetRequest;
import com.connectinghands.service.SecurityService;
import com.connectinghands.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    private UserProfileDto userProfileDto;
    private UpdateProfileRequest updateProfileRequest;
    private PasswordResetRequest passwordResetRequest;

    @BeforeEach
    void setUp() {
        userProfileDto = new UserProfileDto();
        userProfileDto.setId(1L);
        userProfileDto.setEmail("test@example.com");
        userProfileDto.setFirstName("Test");
        userProfileDto.setLastName("User");
        userProfileDto.setPhone("1234567890");
        userProfileDto.setRole("ROLE_USER");
        userProfileDto.setEmailVerified(true);

        updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setFirstName("Updated");
        updateProfileRequest.setLastName("User");
        updateProfileRequest.setPhone("0987654321");

        passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setToken("reset-token");
        passwordResetRequest.setNewPassword("newPassword123");

        when(securityService.getCurrentUserId()).thenReturn(1L);
    }

    @Test
    @WithMockUser
    void getCurrentUserProfile_AuthenticatedUser_ReturnsProfile() throws Exception {
        when(userService.getCurrentUserProfile()).thenReturn(userProfileDto);

        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    void updateProfile_ValidRequest_ReturnsUpdatedProfile() throws Exception {
        when(userService.updateProfile(any(UpdateProfileRequest.class))).thenReturn(userProfileDto);

        mockMvc.perform(put("/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateProfileRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void requestPasswordReset_ValidEmail_ReturnsOk() throws Exception {
        doNothing().when(userService).requestPasswordReset("test@example.com");

        mockMvc.perform(post("/users/request-password-reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_ValidToken_ReturnsOk() throws Exception {
        doNothing().when(userService).resetPassword(eq("reset-token"), eq("newPassword123"));

        mockMvc.perform(post("/users/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(passwordResetRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void verifyEmail_ValidToken_ReturnsOk() throws Exception {
        doNothing().when(userService).verifyEmail("verification-token");

        mockMvc.perform(get("/users/verify-email")
                .param("token", "verification-token"))
                .andExpect(status().isOk());
    }
} 