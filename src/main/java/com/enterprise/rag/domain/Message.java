package com.enterprise.rag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a message in a conversation.
 * Supports user, assistant, system, and tool messages.
 */
@Entity
@Table(name = "messages", schema = "rag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "conversation_id", nullable = false)
  private Conversation conversation;

  @Column(name = "role", nullable = false, length = 50)
  private String role;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "timestamp", nullable = false)
  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  @Column(name = "token_count")
  private Integer tokenCount;

  @Column(name = "metadata", columnDefinition = "jsonb")
  private String metadata;
}
