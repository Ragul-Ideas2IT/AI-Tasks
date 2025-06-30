package com.connectinghands.test;

import com.connectinghands.config.TestConfig;
import com.connectinghands.entity.User;
import com.connectinghands.entity.UserRole;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
public abstract class BaseTest {

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected SecurityService securityService;

    @MockBean
    protected AuditLogService auditLogService;

    protected User testUser;

    @BeforeEach
    protected void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setPassword("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG"); // password
        testUser.setRole(UserRole.ROLE_SYSTEM_ADMIN);

        // Set up mock user repository
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        // Set up mock security service
        Mockito.when(securityService.getCurrentUserId()).thenReturn(1L);
        Mockito.when(securityService.isCurrentUserOrphanageAdmin(Mockito.anyLong())).thenReturn(true);
        Mockito.when(securityService.isCurrentUser(Mockito.anyLong())).thenReturn(true);

        // Set up mock audit log service
        Mockito.doNothing().when(auditLogService).logAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyLong());

        // Set up security context
        Authentication auth = new UsernamePasswordAuthenticationToken(
            testUser,
            "password",
            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
} 