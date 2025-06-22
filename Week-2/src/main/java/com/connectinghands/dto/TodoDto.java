package com.connectinghands.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {
    private int id;
    private String title;
    private String description;
    private boolean completed;
    private Long userId;
} 