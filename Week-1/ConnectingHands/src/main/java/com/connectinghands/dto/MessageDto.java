package com.connectinghands.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for transferring message data.
 * 
 * @author Ragul Venkatesan
 */
@Data
public class MessageDto {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private String content;
    private boolean read;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setId(Long id) { this.id = id; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public void setContent(String content) { this.content = content; }
    public void setRead(boolean read) { this.read = read; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
} 