package com.enterprise.rag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for chat endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

  private String answer;

  private String sessionId;

  private String conversationId;

  private List<String> sources;

  private Map<String, Object> metadata;

  private Integer tokensUsed;

  private LocalDateTime timestamp;

  @Builder.Default
  private Boolean fromCache = false;

  private String model;

  private String conversationTitle;
}
