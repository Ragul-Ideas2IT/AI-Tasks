package com.connectinghands.controller;

import com.connectinghands.dto.UpdateProfileRequest;
import com.connectinghands.dto.UserProfileDto;
import com.connectinghands.dto.PasswordResetRequest;
import com.connectinghands.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void getCurrentUserProfile_AuthenticatedUser_ReturnsProfile() throws Exception {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(1L);
        dto.setEmail("test@example.com");
        dto.setFirstName("Test");
        dto.setLastName("User");
        dto.setRole("ROLE_USER");
        dto.setEmailVerified(true);
        when(userService.getCurrentUserProfile()).thenReturn(dto);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    void updateProfile_ValidRequest_ReturnsUpdatedProfile() throws Exception {
        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setFirstName("Updated");
        req.setLastName("User");
        UserProfileDto dto = new UserProfileDto();
        dto.setId(1L);
        dto.setEmail("test@example.com");
        dto.setFirstName("Updated");
        dto.setLastName("User");
        dto.setRole("ROLE_USER");
        dto.setEmailVerified(true);
        when(userService.updateProfile(any(UpdateProfileRequest.class))).thenReturn(dto);

        mockMvc.perform(put("/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void requestPasswordReset_ValidEmail_ReturnsOk() throws Exception {
        doNothing().when(userService).requestPasswordReset("test@example.com");
        mockMvc.perform(post("/users/request-password-reset")
                .param("email", "test@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void resetPassword_ValidToken_ReturnsOk() throws Exception {
        PasswordResetRequest req = new PasswordResetRequest();
        req.setToken("token");
        req.setNewPassword("newPassword123");
        doNothing().when(userService).resetPassword("token", "newPassword123");
        mockMvc.perform(post("/users/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void verifyEmail_ValidToken_ReturnsOk() throws Exception {
        doNothing().when(userService).verifyEmail("token");
        mockMvc.perform(get("/users/verify-email")
                .param("token", "token"))
                .andExpect(status().isOk());
    }
} 