package com.enterprise.rag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing a conversation session.
 * Stores conversation metadata and related messages.
 */
@Entity
@Table(name = "conversations", schema = "rag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversation {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "user_id", length = 255)
  private String userId;

  @Column(name = "title", length = 500)
  private String title;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "metadata", columnDefinition = "jsonb")
  private String metadata;

  @Column(name = "status", length = 50)
  private String status;

  @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Message> messages = new ArrayList<>();

  /**
   * Helper method to add a message to the conversation.
   *
   * @param message the message to add
   */
  public void addMessage(Message message) {
    messages.add(message);
    message.setConversation(this);
  }
}
