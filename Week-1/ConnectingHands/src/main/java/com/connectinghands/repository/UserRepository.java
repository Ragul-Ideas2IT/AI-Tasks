package com.connectinghands.repository;

import com.connectinghands.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Provides CRUD operations and custom queries for users.
 *
 * @author Ragul Venkatesan
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by their username.
     *
     * @param username The username to search for
     * @return An Optional containing the user if found, empty otherwise
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if a username exists.
     *
     * @param username The username to check
     * @return true if the username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if an email exists.
     *
     * @param email The email to check
     * @return true if the email exists, false otherwise
     */
    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String token);
    Optional<User> findByResetToken(String token);

    Optional<User> findByEmail(String email);
} 