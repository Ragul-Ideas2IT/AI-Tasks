package com.connectinghands.service.impl;

import com.connectinghands.dto.CreateMessageRequest;
import com.connectinghands.dto.MessageDto;
import com.connectinghands.entity.Message;
import com.connectinghands.entity.User;
import com.connectinghands.exception.ResourceNotFoundException;
import com.connectinghands.repository.MessageRepository;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implementation of the MessageService interface.
 * Handles messaging functionality between users and orphanages.
 *
 * @author Ragul Venkatesan
 */
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MessageDto sendMessage(CreateMessageRequest request) {
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setRead(false);

        Message savedMessage = messageRepository.save(message);
        return convertToDto(savedMessage);
    }

    @Override
    public MessageDto getMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        return convertToDto(message);
    }

    @Override
    public Page<MessageDto> getConversation(Long userId1, Long userId2, Pageable pageable) {
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
                userId1, userId2, userId1, userId2, pageable)
                .map(this::convertToDto);
    }

    @Override
    public Page<MessageDto> getInbox(Long userId, Pageable pageable) {
        return messageRepository.findByReceiverId(userId, pageable)
                .map(this::convertToDto);
    }

    @Override
    public Page<MessageDto> getSent(Long userId, Pageable pageable) {
        return messageRepository.findBySenderId(userId, pageable)
                .map(this::convertToDto);
    }

    @Override
    public void markAsRead(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        message.setRead(true);
        messageRepository.save(message);
    }

    @Override
    public Long countUnread(Long userId) {
        return messageRepository.countByReceiverIdAndReadFalse(userId);
    }

    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getName());
        dto.setReceiverId(message.getReceiver().getId());
        dto.setReceiverName(message.getReceiver().getName());
        dto.setContent(message.getContent());
        dto.setRead(message.isRead());
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }
} 