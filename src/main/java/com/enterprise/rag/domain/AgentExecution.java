package com.enterprise.rag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * AgentExecution entity for tracking agent workflows.
 */
@Entity
@Table(name = "agent_executions", schema = "rag")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentExecution {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "conversation_id")
  private Conversation conversation;

  @Column(name = "agent_type", length = 100, nullable = false)
  private String agentType;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "input_data", columnDefinition = "jsonb", nullable = false)
  private Map<String, Object> inputData;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "output_data", columnDefinition = "jsonb")
  private Map<String, Object> outputData;

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

  @OneToMany(mappedBy = "execution", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ToolInvocation> toolInvocations = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    if (startedAt == null) {
      startedAt = LocalDateTime.now();
    }
  }

  public void addToolInvocation(ToolInvocation toolInvocation) {
    toolInvocations.add(toolInvocation);
    toolInvocation.setExecution(this);
  }
}
