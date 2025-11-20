package com.enterprise.rag.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for chat API requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRequest {

  @NotBlank(message = "Query cannot be empty")
  @Size(min = 1, max = 5000, message = "Query must be between 1 and 5000 characters")
  private String query;

  @NotBlank(message = "Session ID cannot be empty")
  private String sessionId;

  private String userId;

  @Builder.Default
  private Integer maxTokens = 1000;

  @Builder.Default
  private Double temperature = 0.7;
}
