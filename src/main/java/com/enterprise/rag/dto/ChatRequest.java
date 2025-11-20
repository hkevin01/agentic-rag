package com.enterprise.rag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Request DTO for chat endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

  @NotBlank(message = "Query cannot be empty")
  @Size(max = 4000, message = "Query too long (max 4000 characters)")
  private String query;

  @NotBlank(message = "Session ID is required")
  private String sessionId;

  private String userId;

  private Map<String, Object> context;

  @Builder.Default
  private Integer maxTokens = 1000;

  @Builder.Default
  private Double temperature = 0.7;

  @Builder.Default
  private Boolean useRag = true;
}
