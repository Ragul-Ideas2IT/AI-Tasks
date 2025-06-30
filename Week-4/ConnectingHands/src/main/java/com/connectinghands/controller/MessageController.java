package com.connectinghands.controller;

import com.connectinghands.dto.CreateMessageRequest;
import com.connectinghands.dto.MessageDto;
import com.connectinghands.service.MessageService;
import com.connectinghands.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing messages between users.
 * Provides endpoints for sending, retrieving, and marking messages as read.
 *
 * @author Ragul Venkatesan
 */
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Message management APIs")
public class MessageController {
    private final MessageService messageService;
    private final SecurityService securityService;

    public MessageController(MessageService messageService, SecurityService securityService) {
        this.messageService = messageService;
        this.securityService = securityService;
    }

    /**
     * Send a new message to another user.
     * Requires authentication.
     *
     * @param request The message creation request
     * @return The created message
     */
    @PostMapping
    @Operation(summary = "Send a message")
    public ResponseEntity<MessageDto> sendMessage(@Valid @RequestBody CreateMessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(request));
    }

    /**
     * Get a paginated conversation between the current user and another user.
     * Requires authentication.
     *
     * @param userId The other user's ID
     * @param pageable Pagination information
     * @return Page of messages
     */
    @GetMapping("/conversation/{userId}")
    @Operation(summary = "Get conversation with a user")
    public ResponseEntity<Page<MessageDto>> getConversation(@PathVariable Long userId, Pageable pageable) {
        Long currentUserId = securityService.getCurrentUser().getId();
        return ResponseEntity.ok(messageService.getConversation(currentUserId, userId, pageable));
    }

    /**
     * Get a paginated list of messages received by the current user.
     * Requires authentication.
     *
     * @param pageable Pagination information
     * @return Page of messages
     */
    @GetMapping("/inbox")
    @Operation(summary = "Get inbox messages")
    public ResponseEntity<Page<MessageDto>> getInbox(Pageable pageable) {
        Long currentUserId = securityService.getCurrentUser().getId();
        return ResponseEntity.ok(messageService.getInbox(currentUserId, pageable));
    }

    /**
     * Get a paginated list of messages sent by the current user.
     * Requires authentication.
     *
     * @param pageable Pagination information
     * @return Page of messages
     */
    @GetMapping("/sent")
    @Operation(summary = "Get sent messages")
    public ResponseEntity<Page<MessageDto>> getSent(Pageable pageable) {
        Long currentUserId = securityService.getCurrentUser().getId();
        return ResponseEntity.ok(messageService.getSent(currentUserId, pageable));
    }

    /**
     * Mark a message as read.
     * Requires authentication and the message must be received by the current user.
     *
     * @param id The message ID
     * @return No content response
     */
    @PutMapping("/{id}/read")
    @Operation(summary = "Mark a message as read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Get the count of unread messages for the current user.
     * Requires authentication.
     *
     * @return The number of unread messages
     */
    @GetMapping("/unread/count")
    @Operation(summary = "Get unread message count")
    public ResponseEntity<Long> countUnread() {
        Long currentUserId = securityService.getCurrentUser().getId();
        return ResponseEntity.ok(messageService.countUnread(currentUserId));
    }
}
