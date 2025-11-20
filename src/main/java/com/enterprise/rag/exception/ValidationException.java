package com.enterprise.rag.exception;

/**
 * Exception thrown when input validation fails.
 * REQ-ERR-010: Custom exception for validation errors.
 */
public class ValidationException extends RuntimeException {

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
