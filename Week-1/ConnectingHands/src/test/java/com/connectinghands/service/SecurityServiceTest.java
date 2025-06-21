package com.connectinghands.service;

import com.connectinghands.entity.User;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private SecurityServiceImpl securityService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
    }

    @Test
    void getCurrentUserId_AuthenticatedUser_ReturnsUserId() {
        when(authentication.getName()).thenReturn("1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long userId = securityService.getCurrentUserId();
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void getCurrentUserId_NoAuthentication_ThrowsException() {
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThatThrownBy(() -> securityService.getCurrentUserId())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No authenticated user found");
    }

    @Test
    void isCurrentUserOrphanageAdmin_ValidOrphanage_ReturnsTrue() {
        when(authentication.getName()).thenReturn("1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        boolean isAdmin = securityService.isCurrentUserOrphanageAdmin(1L);
        assertThat(isAdmin).isTrue();
    }

    @Test
    void isCurrentUserOrphanageAdmin_InvalidOrphanage_ReturnsFalse() {
        when(authentication.getName()).thenReturn("1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        boolean isAdmin = securityService.isCurrentUserOrphanageAdmin(2L);
        assertThat(isAdmin).isFalse();
    }

    @Test
    void isCurrentUser_ValidUserId_ReturnsTrue() {
        when(authentication.getName()).thenReturn("1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        boolean isCurrentUser = securityService.isCurrentUser(1L);
        assertThat(isCurrentUser).isTrue();
    }

    @Test
    void isCurrentUser_InvalidUserId_ReturnsFalse() {
        when(authentication.getName()).thenReturn("1");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        boolean isCurrentUser = securityService.isCurrentUser(2L);
        assertThat(isCurrentUser).isFalse();
    }
} 