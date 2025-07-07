package com.collabdocs.document.model;

import jakarta.persistence.*;

@Entity
@Table(name = "document_shares")
public class DocumentShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    private String sharedWithUsername;
    private String sharedWithEmail;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Document getDocument() { return document; }
    public void setDocument(Document document) { this.document = document; }
    public String getSharedWithUsername() { return sharedWithUsername; }
    public void setSharedWithUsername(String sharedWithUsername) { this.sharedWithUsername = sharedWithUsername; }
    public String getSharedWithEmail() { return sharedWithEmail; }
    public void setSharedWithEmail(String sharedWithEmail) { this.sharedWithEmail = sharedWithEmail; }
} 