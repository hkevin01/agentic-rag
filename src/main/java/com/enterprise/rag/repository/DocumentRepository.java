package com.enterprise.rag.repository;

import com.enterprise.rag.domain.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Document entity.
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

  /**
   * Find document by content hash.
   */
  Optional<Document> findByContentHash(String contentHash);

  /**
   * Find documents by document type.
   */
  List<Document> findByDocumentType(String documentType);

  /**
   * Find documents by status.
   */
  Page<Document> findByStatus(String status, Pageable pageable);

  /**
   * Find documents by source.
   */
  List<Document> findBySource(String source);

  /**
   * Find documents created after a specific date.
   */
  @Query("SELECT d FROM Document d WHERE d.createdAt > :date ORDER BY d.createdAt DESC")
  List<Document> findRecentDocuments(@Param("date") LocalDateTime date);

  /**
   * Find document by ID with chunks eagerly loaded.
   */
  @Query("SELECT d FROM Document d LEFT JOIN FETCH d.chunks WHERE d.id = :id")
  Optional<Document> findByIdWithChunks(@Param("id") UUID id);

  /**
   * Count documents by status.
   */
  long countByStatus(String status);

  /**
   * Check if content hash already exists.
   */
  boolean existsByContentHash(String contentHash);
}
