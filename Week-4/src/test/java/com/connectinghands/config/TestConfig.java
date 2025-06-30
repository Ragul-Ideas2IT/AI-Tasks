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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.Optional;

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
        testUser.setPassword("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG"); // password
        testUser.setRole(UserRole.ROLE_SYSTEM_ADMIN);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
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
    @Primary
    public UserDetailsService userDetailsService() {
        return mock(UserDetailsService.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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

        // Set up test authentication
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRole.ROLE_SYSTEM_ADMIN);

        Authentication auth = new UsernamePasswordAuthenticationToken(
            testUser,
            "password",
            Arrays.asList(new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"))
        );
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        return http.build();
    }
} 