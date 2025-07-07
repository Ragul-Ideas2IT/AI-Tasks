package com.collabdocs.document.service;

import com.collabdocs.document.model.Document;
import com.collabdocs.document.model.DocumentDTO;
import java.util.List;

public interface DocumentService {
    Document createDocument(DocumentDTO documentDTO);
    Document getDocument(Long id);
    List<Document> getAllDocuments();
    Document updateDocument(Long id, DocumentDTO documentDTO);
    void deleteDocument(Long id);
} 