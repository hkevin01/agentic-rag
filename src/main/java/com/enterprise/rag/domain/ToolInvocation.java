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
 * ToolInvocation entity for tracking tool usage.
 */
@Entity
@Table(name = "tool_invocations", schema = "rag")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolInvocation {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "execution_id", nullable = false)
  private AgentExecution execution;

  @Column(name = "tool_name", length = 100, nullable = false)
  private String toolName;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "input_params", columnDefinition = "jsonb", nullable = false)
  private Map<String, Object> inputParams;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "output_result", columnDefinition = "jsonb")
  private Map<String, Object> outputResult;

  @Column(length = 50, nullable = false)
  @Builder.Default
  private String status = "pending";

  @Column(name = "started_at", nullable = false)
  @Builder.Default
  private LocalDateTime startedAt = LocalDateTime.now();

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @Column(name = "error_message", columnDefinition = "TEXT")
  private String errorMessage;

  @Column(name = "execution_time_ms")
  private Integer executionTimeMs;

  @PrePersist
  protected void onCreate() {
    if (startedAt == null) {
      startedAt = LocalDateTime.now();
    }
  }
}
