package com.enterprise.rag.repository;

import com.enterprise.rag.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Message entity.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  /**
   * Find all messages for a conversation.
   */
  List<Message> findByConversationIdOrderByTimestampAsc(UUID conversationId);

  /**
   * Find messages by conversation with pagination.
   */
  Page<Message> findByConversationId(UUID conversationId, Pageable pageable);

  /**
   * Find messages by role in a conversation.
   */
  List<Message> findByConversationIdAndRole(UUID conversationId, String role);

  /**
   * Find recent messages in a conversation.
   */
  @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId " +
         "AND m.timestamp > :since ORDER BY m.timestamp ASC")
  List<Message> findRecentMessages(
      @Param("conversationId") UUID conversationId,
      @Param("since") LocalDateTime since);

  /**
   * Count messages in a conversation.
   */
  long countByConversationId(UUID conversationId);

  /**
   * Sum token counts for a conversation.
   */
  @Query("SELECT COALESCE(SUM(m.tokenCount), 0) FROM Message m WHERE m.conversation.id = :conversationId")
  long sumTokenCountByConversationId(@Param("conversationId") UUID conversationId);
}
