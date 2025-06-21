package com.connectinghands.service;

import com.connectinghands.dto.CreateMessageRequest;
import com.connectinghands.dto.MessageDto;
import com.connectinghands.entity.Message;
import com.connectinghands.entity.User;
import com.connectinghands.repository.MessageRepository;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.impl.MessageServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private MessageServiceImpl messageService;

    private User sender;
    private User receiver;
    private Message message;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1L);
        sender.setEmail("sender@example.com");

        receiver = new User();
        receiver.setId(2L);
        receiver.setEmail("receiver@example.com");

        message = new Message();
        message.setId(1L);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Test message");
        message.setRead(false);
    }

    @Test
    void sendMessage_ValidRequest_ReturnsMessageDto() {
        CreateMessageRequest request = new CreateMessageRequest();
        request.setReceiverId(2L);
        request.setContent("Test message");

        when(authentication.getName()).thenReturn("sender@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        MessageDto dto = messageService.sendMessage(request);
        assertThat(dto).isNotNull();
        assertThat(dto.getSenderId()).isEqualTo(1L);
        assertThat(dto.getReceiverId()).isEqualTo(2L);
        assertThat(dto.getContent()).isEqualTo("Test message");
    }

    @Test
    void sendMessage_SenderNotFound_ThrowsException() {
        CreateMessageRequest request = new CreateMessageRequest();
        request.setReceiverId(2L);

        when(authentication.getName()).thenReturn("nonexistent@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.sendMessage(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Sender not found");
    }

    @Test
    void sendMessage_ReceiverNotFound_ThrowsException() {
        CreateMessageRequest request = new CreateMessageRequest();
        request.setReceiverId(999L);

        when(authentication.getName()).thenReturn("sender@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.sendMessage(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Receiver not found");
    }

    @Test
    void getMessage_ValidId_ReturnsMessageDto() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        MessageDto dto = messageService.getMessage(1L);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getContent()).isEqualTo("Test message");
    }

    @Test
    void getMessage_NotFound_ThrowsException() {
        when(messageRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.getMessage(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Message not found");
    }

    @Test
    void getConversation_ReturnsPageOfMessages() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> messagePage = new PageImpl<>(Collections.singletonList(message));

        when(authentication.getName()).thenReturn("sender@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(messageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtDesc(
                sender, receiver, sender, receiver, pageable)).thenReturn(messagePage);

        Page<MessageDto> dtoPage = messageService.getConversation(2L, pageable);
        assertThat(dtoPage).isNotNull();
        assertThat(dtoPage.getContent()).hasSize(1);
        assertThat(dtoPage.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getInbox_ReturnsPageOfMessages() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> messagePage = new PageImpl<>(Collections.singletonList(message));

        when(authentication.getName()).thenReturn("receiver@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));
        when(messageRepository.findByReceiverOrderByCreatedAtDesc(receiver, pageable)).thenReturn(messagePage);

        Page<MessageDto> dtoPage = messageService.getInbox(pageable);
        assertThat(dtoPage).isNotNull();
        assertThat(dtoPage.getContent()).hasSize(1);
        assertThat(dtoPage.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getSent_ReturnsPageOfMessages() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Message> messagePage = new PageImpl<>(Collections.singletonList(message));

        when(authentication.getName()).thenReturn("sender@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("sender@example.com")).thenReturn(Optional.of(sender));
        when(messageRepository.findBySenderOrderByCreatedAtDesc(sender, pageable)).thenReturn(messagePage);

        Page<MessageDto> dtoPage = messageService.getSent(pageable);
        assertThat(dtoPage).isNotNull();
        assertThat(dtoPage.getContent()).hasSize(1);
        assertThat(dtoPage.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void markAsRead_ValidId_MarksMessageAsRead() {
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        messageService.markAsRead(1L);
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void markAsRead_NotFound_ThrowsException() {
        when(messageRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> messageService.markAsRead(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Message not found");
    }

    @Test
    void countUnread_ReturnsCount() {
        when(authentication.getName()).thenReturn("receiver@example.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(userRepository.findByEmail("receiver@example.com")).thenReturn(Optional.of(receiver));
        when(messageRepository.countByReceiverAndReadFalse(receiver)).thenReturn(5L);

        long count = messageService.countUnread();
        assertThat(count).isEqualTo(5L);
    }
} 