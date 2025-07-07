package com.collabdocs.document.controller;

import com.collabdocs.document.model.Document;
import com.collabdocs.document.model.DocumentDTO;
import com.collabdocs.document.model.DocumentVersion;
import com.collabdocs.document.model.DocumentShare;
import com.collabdocs.document.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(@Valid @RequestBody DocumentDTO documentDTO) {
        return ResponseEntity.ok(documentService.createDocument(documentDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getDocument(id));
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @Valid @RequestBody DocumentDTO documentDTO) {
        return ResponseEntity.ok(documentService.updateDocument(id, documentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<DocumentVersion>> getVersions(@PathVariable Long id) {
        return ResponseEntity.ok(((com.collabdocs.document.service.DocumentServiceImpl)documentService).getVersions(id));
    }

    @PostMapping("/{id}/restore/{versionId}")
    public ResponseEntity<Document> restoreVersion(@PathVariable Long id, @PathVariable Long versionId) {
        return ResponseEntity.ok(((com.collabdocs.document.service.DocumentServiceImpl)documentService).restoreVersion(id, versionId));
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<DocumentShare> shareDocument(@PathVariable Long id, @RequestParam(required = false) String username, @RequestParam(required = false) String email) {
        return ResponseEntity.ok(((com.collabdocs.document.service.DocumentServiceImpl)documentService).shareDocument(id, username, email));
    }

    @GetMapping("/{id}/shares")
    public ResponseEntity<List<DocumentShare>> getShares(@PathVariable Long id) {
        return ResponseEntity.ok(((com.collabdocs.document.service.DocumentServiceImpl)documentService).getShares(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Document>> searchDocuments(@RequestParam String keyword) {
        return ResponseEntity.ok(((com.collabdocs.document.service.DocumentServiceImpl)documentService).searchDocuments(keyword));
    }
} 