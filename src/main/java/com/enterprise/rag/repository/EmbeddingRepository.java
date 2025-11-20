package com.enterprise.rag.repository;

import com.enterprise.rag.domain.Embedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Embedding entity with vector similarity search.
 */
@Repository
public interface EmbeddingRepository extends JpaRepository<Embedding, UUID> {

  /**
   * Find embedding by chunk and model.
   */
  Optional<Embedding> findByChunkIdAndModelName(UUID chunkId, String modelName);

  /**
   * Find all embeddings for a chunk.
   */
  List<Embedding> findByChunkId(UUID chunkId);

  /**
   * Vector similarity search using cosine distance.
   * Returns the top K most similar embeddings.
   */
  @Query(value = "SELECT e.id, e.chunk_id, e.embedding, e.model_name, e.created_at, " +
                 "1 - (e.embedding <=> CAST(:queryVector AS vector)) AS similarity " +
                 "FROM rag.embeddings e " +
                 "WHERE e.model_name = :modelName " +
                 "ORDER BY e.embedding <=> CAST(:queryVector AS vector) " +
                 "LIMIT :limit",
         nativeQuery = true)
  List<Object[]> findSimilarEmbeddingsCosine(
      @Param("queryVector") String queryVector,
      @Param("modelName") String modelName,
      @Param("limit") int limit);

  /**
   * Vector similarity search using L2 (Euclidean) distance.
   */
  @Query(value = "SELECT e.id, e.chunk_id, e.embedding, e.model_name, e.created_at, " +
                 "e.embedding <-> CAST(:queryVector AS vector) AS distance " +
                 "FROM rag.embeddings e " +
                 "WHERE e.model_name = :modelName " +
                 "ORDER BY e.embedding <-> CAST(:queryVector AS vector) " +
                 "LIMIT :limit",
         nativeQuery = true)
  List<Object[]> findSimilarEmbeddingsL2(
      @Param("queryVector") String queryVector,
      @Param("modelName") String modelName,
      @Param("limit") int limit);

  /**
   * Count embeddings by model.
   */
  long countByModelName(String modelName);
}
