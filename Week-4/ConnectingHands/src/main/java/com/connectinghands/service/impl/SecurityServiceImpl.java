package com.connectinghands.service.impl;

import com.connectinghands.entity.User;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.SecurityService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

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
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
            .orElseThrow(() -> new AccessDeniedException("User not found"));
    }

    @Override
    public boolean isCurrentUser(Long userId) {
        try {
            Long currentUserId = getCurrentUserId();
            return currentUserId.equals(userId);
        } catch (AccessDeniedException e) {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    @Override
    public boolean isCurrentUserOrphanageAdmin(Long orphanageId) {
        try {
            User currentUser = getCurrentUser();
            // Check if user has orphanage admin role and is associated with the orphanage
            return currentUser.getRole().name().equals("ROLE_ORPHANAGE_ADMIN") && 
                   currentUser.getOrphanage() != null && 
                   currentUser.getOrphanage().getId().equals(orphanageId);
        } catch (AccessDeniedException e) {
            return false;
        }
    }

    /**
     * Retrieves the ID of the currently authenticated user from the Spring Security context.
     * Throws AccessDeniedException if no authenticated user is found or if the user ID is invalid.
     *
     * @return the ID of the current authenticated user
     * @throws AccessDeniedException if authentication is missing or invalid
     */
    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            try {
                if (authentication.getPrincipal() instanceof User) {
                    return ((User) authentication.getPrincipal()).getId();
                } else {
                    return Long.parseLong(authentication.getName());
                }
            } catch (NumberFormatException e) {
                throw new AccessDeniedException("Invalid user ID in authentication context");
            }
        }
        throw new AccessDeniedException("No authenticated user found");
    }
}