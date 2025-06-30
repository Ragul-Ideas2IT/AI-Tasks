package com.connectinghands.repository;

import com.connectinghands.entity.Message;
import com.connectinghands.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Message entity.
 * 
 * @author Ragul Venkatesan
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Find all messages between two users.
     *
     * @param user1 First user
     * @param user2 Second user
     * @param pageable Pagination information
     * @return Page of messages
     */
    Page<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtDesc(
            User user1, User user2, User user3, User user4, Pageable pageable);

    /**
     * Find all unread messages for a user.
     *
     * @param user User to find unread messages for
     * @return List of unread messages
     */
    List<Message> findByReceiverAndReadFalseOrderByCreatedAtDesc(User user);

    /**
     * Count unread messages for a user.
     *
     * @param user User to count unread messages for
     * @return Number of unread messages
     */
    long countByReceiverAndReadFalse(User user);

    /**
     * Find all messages sent by a user.
     *
     * @param sender User who sent the messages
     * @param pageable Pagination information
     * @return Page of messages
     */
    Page<Message> findBySenderOrderByCreatedAtDesc(User sender, Pageable pageable);

    /**
     * Find all messages received by a user.
     *
     * @param receiver User who received the messages
     * @param pageable Pagination information
     * @return Page of messages
     */
    Page<Message> findByReceiverOrderByCreatedAtDesc(User receiver, Pageable pageable);

    Page<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
        Long senderId1, Long receiverId1, Long senderId2, Long receiverId2, Pageable pageable);
    Page<Message> findByReceiverId(Long receiverId, Pageable pageable);
    Page<Message> findBySenderId(Long senderId, Pageable pageable);
    Long countByReceiverIdAndReadFalse(Long receiverId);
}
