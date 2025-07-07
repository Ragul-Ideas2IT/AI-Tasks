package com.collabdocs.document.repository;

import com.collabdocs.document.model.DocumentShare;
import com.collabdocs.document.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentShareRepository extends JpaRepository<DocumentShare, Long> {
    List<DocumentShare> findByDocument(Document document);
} 