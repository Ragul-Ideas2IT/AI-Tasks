package com.connectinghands.service;

import com.connectinghands.entity.User;
import com.connectinghands.entity.UserRole;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityServiceImpl securityService;

    private User testUser;
    private Authentication authentication;
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRole.ROLE_SYSTEM_ADMIN);

        // Set up security context with test user
        authentication = new UsernamePasswordAuthenticationToken(
            testUser,
            "password",
            Arrays.asList(new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"))
        );
        securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
    }

    @Test
    void getCurrentUserId_AuthenticatedUser_ReturnsUserId() {
        Long userId = securityService.getCurrentUserId();
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void getCurrentUserId_NoAuthentication_ThrowsException() {
        SecurityContextHolder.clearContext();

        assertThatThrownBy(() -> securityService.getCurrentUserId())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No authenticated user found");
    }

    @Test
    void isCurrentUserOrphanageAdmin_ValidOrphanage_ReturnsTrue() {
        testUser.setRole(UserRole.ROLE_ORPHANAGE_ADMIN);
        testUser.setOrphanageId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean isAdmin = securityService.isCurrentUserOrphanageAdmin(1L);
        assertThat(isAdmin).isTrue();
    }

    @Test
    void isCurrentUserOrphanageAdmin_InvalidOrphanage_ReturnsFalse() {
        testUser.setRole(UserRole.ROLE_ORPHANAGE_ADMIN);
        testUser.setOrphanageId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        boolean isAdmin = securityService.isCurrentUserOrphanageAdmin(2L);
        assertThat(isAdmin).isFalse();
    }

    @Test
    void isCurrentUser_ValidUserId_ReturnsTrue() {
        boolean isCurrentUser = securityService.isCurrentUser(1L);
        assertThat(isCurrentUser).isTrue();
    }

    @Test
    void isCurrentUser_InvalidUserId_ReturnsFalse() {
        boolean isCurrentUser = securityService.isCurrentUser(2L);
        assertThat(isCurrentUser).isFalse();
    }
} 