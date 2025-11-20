package com.enterprise.rag.repository;

import com.enterprise.rag.domain.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for DocumentChunk entity.
 */
@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {

  /**
   * Find all chunks for a document ordered by index.
   */
  List<DocumentChunk> findByDocumentIdOrderByChunkIndexAsc(UUID documentId);

  /**
   * Find a specific chunk by document and index.
   */
  @Query("SELECT c FROM DocumentChunk c WHERE c.document.id = :documentId AND c.chunkIndex = :index")
  DocumentChunk findByDocumentIdAndChunkIndex(
      @Param("documentId") UUID documentId,
      @Param("index") Integer index);

  /**
   * Count chunks for a document.
   */
  long countByDocumentId(UUID documentId);
}
