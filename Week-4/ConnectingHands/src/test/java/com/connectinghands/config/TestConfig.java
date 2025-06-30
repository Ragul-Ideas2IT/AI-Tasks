package com.connectinghands.config;

import com.connectinghands.entity.User;
import com.connectinghands.entity.UserRole;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.SecurityService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
@EnableWebSecurity
public class TestConfig {

    @Bean
    @Primary
    public SecurityService securityService() {
        SecurityService securityService = mock(SecurityService.class);
        when(securityService.getCurrentUserId()).thenReturn(1L);
        when(securityService.isCurrentUser(anyLong())).thenReturn(true);
        when(securityService.isCurrentUserOrphanageAdmin(anyLong())).thenReturn(true);
        return securityService;
    }

    @Bean
    @Primary
    public UserRepository userRepository() {
        UserRepository userRepository = mock(UserRepository.class);
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRole.ROLE_SYSTEM_ADMIN);
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(testUser));
        return userRepository;
    }

    @Bean
    @Primary
    public AuditLogService auditLogService() {
        AuditLogService auditLogService = mock(AuditLogService.class);
        doNothing().when(auditLogService).logAction(anyString(), anyString(), any());
        return auditLogService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/**").hasRole("SYSTEM_ADMIN")
                .requestMatchers("/api/resources/**").hasRole("SYSTEM_ADMIN")
                .requestMatchers("/api/orphanages/**").hasRole("SYSTEM_ADMIN")
                .requestMatchers("/api/donations/**").hasRole("SYSTEM_ADMIN")
                .anyRequest().authenticated()
            );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                "test@example.com",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"))
            );
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(auth);
            SecurityContextHolder.setContext(securityContext);
            return auth;
        };
    }
} 