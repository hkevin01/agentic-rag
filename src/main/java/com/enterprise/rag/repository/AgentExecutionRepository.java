package com.enterprise.rag.repository;

import com.enterprise.rag.domain.AgentExecution;
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
 * Repository interface for AgentExecution entity.
 */
@Repository
public interface AgentExecutionRepository extends JpaRepository<AgentExecution, UUID> {

  /**
   * Find executions by conversation.
   */
  List<AgentExecution> findByConversationIdOrderByStartedAtDesc(UUID conversationId);

  /**
   * Find executions by agent type.
   */
  Page<AgentExecution> findByAgentType(String agentType, Pageable pageable);

  /**
   * Find executions by status.
   */
  List<AgentExecution> findByStatus(String status);

  /**
   * Find recent executions.
   */
  @Query("SELECT e FROM AgentExecution e WHERE e.startedAt > :since ORDER BY e.startedAt DESC")
  List<AgentExecution> findRecentExecutions(@Param("since") LocalDateTime since);

  /**
   * Find failed executions.
   */
  @Query("SELECT e FROM AgentExecution e WHERE e.status = 'failed' ORDER BY e.startedAt DESC")
  List<AgentExecution> findFailedExecutions();

  /**
   * Calculate average execution time by agent type.
   */
  @Query("SELECT AVG(e.executionTimeMs) FROM AgentExecution e WHERE e.agentType = :agentType AND e.executionTimeMs IS NOT NULL")
  Double getAverageExecutionTime(@Param("agentType") String agentType);

  /**
   * Count executions by status.
   */
  long countByStatus(String status);
}
