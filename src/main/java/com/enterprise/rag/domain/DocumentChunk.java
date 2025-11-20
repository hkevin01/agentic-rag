package com.enterprise.rag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DocumentChunk entity representing chunked pieces of documents for RAG retrieval.
 */
@Entity
@Table(name = "document_chunks", schema = "rag",
       uniqueConstraints = @UniqueConstraint(columnNames = {"document_id", "chunk_index"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChunk {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "document_id", nullable = false)
  private Document document;

  @Column(name = "chunk_index", nullable = false)
  private Integer chunkIndex;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "token_count")
  private Integer tokenCount;

  @Column(name = "start_char")
  private Integer startChar;

  @Column(name = "end_char")
  private Integer endChar;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> metadata;

  @OneToMany(mappedBy = "chunk", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Embedding> embeddings = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    if (createdAt == null) {
      createdAt = LocalDateTime.now();
    }
  }
}
