package com.enterprise.rag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for chat API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {

  private String answer;

  private String sessionId;

  private List<String> sources;

  private Map<String, Object> metadata;

  @Builder.Default
  private LocalDateTime timestamp = LocalDateTime.now();

  private Integer tokensUsed;

  private String model;
}
