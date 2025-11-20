package com.enterprise.rag.repository;

import com.enterprise.rag.domain.ToolInvocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for ToolInvocation entity.
 */
@Repository
public interface ToolInvocationRepository extends JpaRepository<ToolInvocation, UUID> {

  /**
   * Find all invocations for an execution.
   */
  List<ToolInvocation> findByExecutionIdOrderByStartedAtAsc(UUID executionId);

  /**
   * Find invocations by tool name.
   */
  List<ToolInvocation> findByToolName(String toolName);

  /**
   * Find invocations by status.
   */
  List<ToolInvocation> findByStatus(String status);

  /**
   * Count invocations by tool name.
   */
  long countByToolName(String toolName);

  /**
   * Calculate average execution time for a tool.
   */
  @Query("SELECT AVG(t.executionTimeMs) FROM ToolInvocation t WHERE t.toolName = :toolName AND t.executionTimeMs IS NOT NULL")
  Double getAverageExecutionTime(@Param("toolName") String toolName);
}
