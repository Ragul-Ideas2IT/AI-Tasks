package com.collabdocs.document.repository;

import com.collabdocs.document.model.DocumentVersion;
import com.collabdocs.document.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {
    List<DocumentVersion> findByDocumentOrderByVersionNumberDesc(Document document);
    DocumentVersion findTopByDocumentOrderByVersionNumberDesc(Document document);
} 