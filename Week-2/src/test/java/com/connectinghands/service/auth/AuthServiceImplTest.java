package com.connectinghands.service.auth;

import com.connectinghands.dto.user.LoginDto;
import com.connectinghands.dto.user.RegisterDto;
import com.connectinghands.entity.Role;
import com.connectinghands.entity.User;
import com.connectinghands.exception.ApiException;
import com.connectinghands.repository.user.RoleRepository;
import com.connectinghands.repository.user.UserRepository;
import com.connectinghands.security.JwtTokenProvider;
import com.connectinghands.service.auth.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginDto loginDto;
    private RegisterDto registerDto;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        registerDto = new RegisterDto();
        registerDto.setFirstName("Test");
        registerDto.setLastName("User");
        registerDto.setEmail("new@example.com");
        registerDto.setPassword("password");

        user = new User();
        user.setEmail(loginDto.getEmail());
        user.setPassword("encodedPassword");

        role = new Role();
        role.setName("ROLE_USER");
    }

    @Test
    public void login_shouldReturnToken_whenCredentialsAreValid() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("test-token");

        String token = authService.login(loginDto);

        assertEquals("test-token", token);
    }

    @Test
    public void register_shouldReturnSuccessMessage_whenEmailIsNotTaken() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(new Role()));

        String result = authService.register(registerDto);

        assertEquals("User registered successfully!", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void register_shouldThrowApiException_whenEmailIsTaken() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(ApiException.class, () -> {
            authService.register(registerDto);
        });
    }
} 