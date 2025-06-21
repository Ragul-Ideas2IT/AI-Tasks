package com.connectinghands.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoDto {
    private Integer id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 