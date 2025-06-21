package com.connectinghands.controller.auth;

import com.connectinghands.dto.user.JwtAuthResponseDto;
import com.connectinghands.dto.user.LoginDto;
import com.connectinghands.dto.user.RegisterDto;
import com.connectinghands.security.JwtAuthenticationFilter;
import com.connectinghands.security.JwtTokenProvider;
import com.connectinghands.security.SecurityConfig;
import com.connectinghands.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void login_shouldReturnToken_whenCredentialsAreValid() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        String token = "test-jwt-token";
        JwtAuthResponseDto responseDto = new JwtAuthResponseDto();
        responseDto.setAccessToken(token);

        when(authService.login(any(LoginDto.class))).thenReturn(token);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(token))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    public void register_shouldReturnSuccessMessage_whenRegistrationIsSuccessful() throws Exception {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstName("Test");
        registerDto.setLastName("User");
        registerDto.setEmail("test@example.com");
        registerDto.setPassword("password");

        String successMessage = "User registered successfully!.";
        when(authService.register(any(RegisterDto.class))).thenReturn(successMessage);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(successMessage));
    }
} 