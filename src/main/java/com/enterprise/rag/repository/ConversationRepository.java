package com.enterprise.rag.repository;

import com.enterprise.rag.domain.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Conversation entity.
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

  /**
   * Find all conversations for a given user.
   */
  List<Conversation> findByUserId(String userId);

  /**
   * Find all conversations for a given user with pagination.
   */
  Page<Conversation> findByUserId(String userId, Pageable pageable);

  /**
   * Find all conversations by status.
   */
  List<Conversation> findByStatus(String status);

  /**
   * Find conversations by user and status.
   */
  Page<Conversation> findByUserIdAndStatus(String userId, String status, Pageable pageable);

  /**
   * Find conversations created after a specific date.
   */
  @Query("SELECT c FROM Conversation c WHERE c.createdAt > :date ORDER BY c.createdAt DESC")
  List<Conversation> findRecentConversations(@Param("date") LocalDateTime date);

  /**
   * Find conversation by ID with messages eagerly loaded.
   */
  @Query("SELECT c FROM Conversation c LEFT JOIN FETCH c.messages WHERE c.id = :id")
  Optional<Conversation> findByIdWithMessages(@Param("id") UUID id);

  /**
   * Count conversations by user.
   */
  long countByUserId(String userId);
}
