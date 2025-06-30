package com.connectinghands.service;

import com.connectinghands.entity.User;

/**
 * Service interface for handling security-related operations.
 * Provides methods for user authentication and authorization.
 *
 * @author Ragul Venkatesan
 */
public interface SecurityService {
    /**
     * Get the ID of the currently authenticated user.
     *
     * @return The ID of the current user
     */
    Long getCurrentUserId();

    /**
     * Check if the current user is associated with a specific orphanage.
     *
     * @param orphanageId the ID of the orphanage to check
     * @return true if the current user is associated with the orphanage
     */
    boolean isCurrentUserOrphanageAdmin(Long orphanageId);

    /**
     * Check if the current user is the specified user.
     *
     * @param userId The ID of the user to check
     * @return true if the current user matches the specified user ID
     */
    boolean isCurrentUser(Long userId);

    User getCurrentUser();
}
