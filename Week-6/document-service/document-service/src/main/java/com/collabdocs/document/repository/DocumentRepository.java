package com.collabdocs.document.repository;

import com.collabdocs.document.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByTitleContainingIgnoreCase(String keyword);
    List<Document> findByContentContainingIgnoreCase(String keyword);
} 