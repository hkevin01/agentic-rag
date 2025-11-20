package com.enterprise.rag.exception;

/**
 * Exception thrown when a requested resource is not found.
 * REQ-ERR-009: Custom exception for 404 scenarios.
 */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

  public ResourceNotFoundException(String resourceType, String resourceId) {
    super(resourceType + " with ID '" + resourceId + "' not found");
  }

  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
