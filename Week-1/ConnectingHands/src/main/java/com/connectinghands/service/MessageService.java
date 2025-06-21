package com.connectinghands.service;

import com.connectinghands.dto.CreateMessageRequest;
import com.connectinghands.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing messages between users (orphanages).
 * Provides methods for sending, retrieving, and marking messages as read.
 *
 * @author Ragul Venkatesan
 */
public interface MessageService {
    /**
     * Send a new message from the current user to a receiver.
     *
     * @param request The request containing sender and receiver IDs and message content
     * @return The created message DTO
     */
    MessageDto sendMessage(CreateMessageRequest request);

    /**
     * Get a message by its ID.
     *
     * @param id The message ID
     * @return The message DTO
     */
    MessageDto getMessage(Long id);

    /**
     * Get a paginated list of messages between two users.
     *
     * @param userId1 The first user's ID
     * @param userId2 The second user's ID
     * @param pageable Pagination information
     * @return Page of message DTOs
     */
    Page<MessageDto> getConversation(Long userId1, Long userId2, Pageable pageable);

    /**
     * Get a paginated list of messages received by a user.
     *
     * @param userId The user's ID
     * @param pageable Pagination information
     * @return Page of message DTOs
     */
    Page<MessageDto> getInbox(Long userId, Pageable pageable);

    /**
     * Get a paginated list of messages sent by a user.
     *
     * @param userId The user's ID
     * @param pageable Pagination information
     * @return Page of message DTOs
     */
    Page<MessageDto> getSent(Long userId, Pageable pageable);

    /**
     * Mark a message as read by the current user.
     *
     * @param id The message ID
     */
    void markAsRead(Long id);

    /**
     * Count the number of unread messages for a user.
     *
     * @param userId The user's ID
     * @return Number of unread messages
     */
    Long countUnread(Long userId);
} 