package com.enterprise.rag.exception;

/**
 * Exception thrown when a service operation fails.
 * REQ-ERR-011: Custom exception for service-level errors.
 */
public class ServiceException extends RuntimeException {

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
