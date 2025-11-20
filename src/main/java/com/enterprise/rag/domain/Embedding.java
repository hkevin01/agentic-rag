package com.enterprise.rag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Embedding entity for storing vector representations of document chunks.
 */
@Entity
@Table(name = "embeddings", schema = "rag",
       uniqueConstraints = @UniqueConstraint(columnNames = {"chunk_id", "model_name"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Embedding {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chunk_id", nullable = false)
  private DocumentChunk chunk;

  @Column(columnDefinition = "vector(1536)")
  private float[] embedding;

  @Column(name = "model_name", length = 100, nullable = false)
  private String modelName;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @PrePersist
  protected void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }
}
