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
 * Document entity representing ingested documents.
 */
@Entity
@Table(name = "documents", schema = "rag")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(length = 1000)
  private String title;

  @Column(length = 500)
  private String source;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "content_hash", length = 64, unique = true)
  private String contentHash;

  @Column(name = "document_type", length = 100)
  private String documentType;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> metadata;

  @Column(length = 50)
  @Builder.Default
  private String status = "indexed";

  @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<DocumentChunk> chunks = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }

  public void addChunk(DocumentChunk chunk) {
    chunks.add(chunk);
    chunk.setDocument(this);
  }

  public void removeChunk(DocumentChunk chunk) {
    chunks.remove(chunk);
    chunk.setDocument(null);
  }
}
