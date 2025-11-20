package com.enterprise.rag.repository;

import com.enterprise.rag.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for Message entity operations.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  /**
   * Find all messages in a conversation ordered by timestamp.
   *
   * @param conversationId the conversation ID
   * @return list of messages
   */
  List<Message> findByConversationIdOrderByTimestampAsc(UUID conversationId);

  /**
   * Count messages in a conversation.
   *
   * @param conversationId the conversation ID
   * @return message count
   */
  long countByConversationId(UUID conversationId);
}
