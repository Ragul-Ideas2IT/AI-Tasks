package com.connectinghands.controller;

import com.connectinghands.dto.CreateMessageRequest;
import com.connectinghands.dto.MessageDto;
import com.connectinghands.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for MessageController.
 * Tests all endpoints with proper security constraints and validation.
 *
 * @author Ragul Venkatesan
 */
@WebMvcTest(MessageController.class)
class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @Test
    @WithMockUser
    void sendMessage_ValidRequest_ReturnsCreatedMessage() throws Exception {
        CreateMessageRequest request = new CreateMessageRequest();
        request.setReceiverId(2L);
        request.setContent("Test message");

        MessageDto response = new MessageDto();
        response.setId(1L);
        response.setSenderId(1L);
        response.setSenderName("Test User");
        response.setReceiverId(2L);
        response.setReceiverName("Receiver User");
        response.setContent("Test message");
        response.setRead(false);

        when(messageService.sendMessage(any(CreateMessageRequest.class))).thenReturn(response);

        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Test message"));
    }

    @Test
    @WithMockUser
    void getConversation_ValidRequest_ReturnsMessages() throws Exception {
        MessageDto message = new MessageDto();
        message.setId(1L);
        message.setContent("Test message");
        Page<MessageDto> page = new PageImpl<>(List.of(message));

        when(messageService.getConversation(eq(1L), eq(2L), any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/messages/conversation/2")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].content").value("Test message"));
    }

    @Test
    @WithMockUser
    void getInbox_ValidRequest_ReturnsMessages() throws Exception {
        MessageDto message = new MessageDto();
        message.setId(1L);
        message.setContent("Test message");
        Page<MessageDto> page = new PageImpl<>(List.of(message));

        when(messageService.getInbox(eq(1L), any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/messages/inbox")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].content").value("Test message"));
    }

    @Test
    @WithMockUser
    void getSent_ValidRequest_ReturnsMessages() throws Exception {
        MessageDto message = new MessageDto();
        message.setId(1L);
        message.setContent("Test message");
        Page<MessageDto> page = new PageImpl<>(List.of(message));

        when(messageService.getSent(eq(1L), any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/messages/sent")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].content").value("Test message"));
    }

    @Test
    @WithMockUser
    void markAsRead_ValidRequest_ReturnsNoContent() throws Exception {
        mockMvc.perform(put("/messages/1/read"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void getUnreadCount_ValidRequest_ReturnsCount() throws Exception {
        when(messageService.countUnread(1L)).thenReturn(5L);

        mockMvc.perform(get("/messages/unread/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void sendMessage_Unauthenticated_ReturnsUnauthorized() throws Exception {
        CreateMessageRequest request = new CreateMessageRequest();
        request.setReceiverId(2L);
        request.setContent("Test message");

        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
} 