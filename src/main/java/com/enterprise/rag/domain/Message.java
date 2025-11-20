package com.enterprise.rag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Message entity representing individual chat messages.
 */
@Entity
@Table(name = "messages", schema = "rag")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "conversation_id", nullable = false)
  private Conversation conversation;

  @Column(length = 50, nullable = false)
  private String role;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  @Column(name = "token_count")
  private Integer tokenCount;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> metadata;

  @PrePersist
  protected void onCreate() {
    if (timestamp == null) {
      timestamp = LocalDateTime.now();
    }
  }
}
