package com.enterprise.rag.repository;

import com.enterprise.rag.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Conversation entity operations.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

  /**
   * Find all conversations for a specific user.
   *
   * @param userId the user ID
   * @return list of conversations
   */
  List<Conversation> findByUserIdOrderByCreatedAtDesc(String userId);

  /**
   * Find conversation by ID and user ID.
   *
   * @param id the conversation ID
   * @param userId the user ID
   * @return optional conversation
   */
  Optional<Conversation> findByIdAndUserId(UUID id, String userId);

  /**
   * Find active conversations for a user.
   *
   * @param userId the user ID
   * @param status the status
   * @return list of conversations
   */
  List<Conversation> findByUserIdAndStatusOrderByUpdatedAtDesc(String userId, String status);
}
