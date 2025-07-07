package com.collabdocs.document.model;

import jakarta.validation.constraints.NotBlank;

public class DocumentDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    @NotBlank(message = "Owner is required")
    private String owner;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
} 