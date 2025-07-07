package com.collabdocs.document.service;

import com.collabdocs.document.model.Document;
import com.collabdocs.document.model.DocumentDTO;
import com.collabdocs.document.model.DocumentShare;
import com.collabdocs.document.repository.DocumentRepository;
import com.collabdocs.document.repository.DocumentShareRepository;
import com.collabdocs.document.repository.DocumentVersionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;
    private final DocumentShareRepository documentShareRepository;
    private final RedisTemplate<String, Document> redisTemplate;
    @Value("${spring.application.name:document-service}")
    private String appName;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentVersionRepository documentVersionRepository, DocumentShareRepository documentShareRepository, RedisTemplate<String, Document> redisTemplate) {
        this.documentRepository = documentRepository;
        this.documentVersionRepository = documentVersionRepository;
        this.documentShareRepository = documentShareRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Document createDocument(DocumentDTO documentDTO) {
        Document doc = new Document();
        doc.setTitle(documentDTO.getTitle());
        doc.setContent(documentDTO.getContent());
        doc.setOwner(documentDTO.getOwner());
        return documentRepository.save(doc);
    }

    @Override
    public Document getDocument(Long id) {
        String cacheKey = appName + ":doc:" + id;
        Document cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) return cached;
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));
        redisTemplate.opsForValue().set(cacheKey, doc);
        return doc;
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document updateDocument(Long id, DocumentDTO documentDTO) {
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));
        doc.setTitle(documentDTO.getTitle());
        doc.setContent(documentDTO.getContent());
        doc.setOwner(documentDTO.getOwner());
        return documentRepository.save(doc);
    }

    @Override
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new EntityNotFoundException("Document not found");
        }
        documentRepository.deleteById(id);
    }

    public DocumentShare shareDocument(Long documentId, String username, String email) {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));
        DocumentShare share = new DocumentShare();
        share.setDocument(doc);
        share.setSharedWithUsername(username);
        share.setSharedWithEmail(email);
        return documentShareRepository.save(share);
    }

    public java.util.List<DocumentShare> getShares(Long documentId) {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));
        return documentShareRepository.findByDocument(doc);
    }

    public java.util.List<Document> searchDocuments(String keyword) {
        java.util.List<Document> byTitle = documentRepository.findByTitleContainingIgnoreCase(keyword);
        java.util.List<Document> byContent = documentRepository.findByContentContainingIgnoreCase(keyword);
        java.util.Set<Document> result = new java.util.HashSet<>(byTitle);
        result.addAll(byContent);
        return new java.util.ArrayList<>(result);
    }
} 