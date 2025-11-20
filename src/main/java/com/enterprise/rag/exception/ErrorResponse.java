package com.enterprise.rag.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response structure for API endpoints.
 * REQ-ERR-008: Consistent error response format across all endpoints.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  /**
   * Timestamp when the error occurred (ISO-8601 format).
   */
  private LocalDateTime timestamp;

  /**
   * HTTP status code.
   */
  private int status;

  /**
   * Error type or category.
   */
  private String error;

  /**
   * Human-readable error message.
   */
  private String message;

  /**
   * Field-level validation errors (if applicable).
   */
  private Map<String, String> fieldErrors;

  /**
   * Request path where error occurred (optional).
   */
  private String path;

  /**
   * Trace ID for debugging (optional).
   */
  private String traceId;
}
