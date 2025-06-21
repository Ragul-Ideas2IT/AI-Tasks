package com.connectinghands.service.impl;

import com.connectinghands.entity.User;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of SecurityService.
 * Handles security-related operations using Spring Security context.
 *
 * @author Ragul Venkatesan
 */
@Service
public class SecurityServiceImpl implements SecurityService, UserDetailsService {
    private final UserRepository userRepository;

    public SecurityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        // TODO: Implement when Spring Security context is integrated
        return null;
    }

    @Override
    public boolean isCurrentUser(Long userId) {
        // TODO: Implement when Spring Security context is integrated
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @Override
    public boolean isOrphanageUser(Long orphanageId) {
        // Dummy implementation for now
        return false;
    }

    @Override
    public boolean isCurrentUserOrphanageAdmin(Long orphanageId) {
        // Dummy implementation for now
        return false;
    }
} 