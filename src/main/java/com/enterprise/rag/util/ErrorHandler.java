package com.enterprise.rag.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized error handling and logging utility.
 * 
 * REQ-ERR-001: All errors must be logged with context for debugging
 * REQ-ERR-002: Provide retry logic for transient failures
 * REQ-ERR-003: Return meaningful error messages to users
 */
@Slf4j
public final class ErrorHandler {

  private ErrorHandler() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Log an error with full context.
   * REQ-ERR-001: Include timestamp, method, and input data in error logs.
   * 
   * @param methodName the name of the method where error occurred
   * @param errorMessage the error message
   * @param exception the exception that was thrown
   * @param context additional context data
   */
  public static void logError(String methodName, String errorMessage, 
                              Exception exception, Map<String, Object> context) {
    Map<String, Object> logContext = new HashMap<>();
    logContext.put("timestamp", LocalDateTime.now());
    logContext.put("method", methodName);
    logContext.put("errorMessage", errorMessage);
    logContext.put("exceptionType", exception.getClass().getSimpleName());
    logContext.put("exceptionMessage", exception.getMessage());
    
    if (context != null) {
      logContext.putAll(context);
    }

    log.error("Error occurred: {}", logContext, exception);
  }

  /**
   * Log an error without additional context.
   * 
   * @param methodName the name of the method where error occurred
   * @param errorMessage the error message
   * @param exception the exception that was thrown
   */
  public static void logError(String methodName, String errorMessage, Exception exception) {
    logError(methodName, errorMessage, exception, null);
  }

  /**
   * Execute an operation with retry logic for transient failures.
   * REQ-ERR-002: Implement retry mechanism for database and network operations.
   * 
   * @param operation the operation to execute
   * @param maxRetries maximum number of retry attempts
   * @param retryDelayMs delay between retries in milliseconds
   * @param operationName name of the operation for logging
   * @param <T> the return type of the operation
   * @return the result of the operation
   * @throws Exception if all retries fail
   */
  public static <T> T executeWithRetry(RetryableOperation<T> operation, 
                                       int maxRetries, 
                                       long retryDelayMs,
                                       String operationName) throws Exception {
    ValidationUtils.requirePositive(maxRetries, "maxRetries");
    ValidationUtils.requireNonNegative(retryDelayMs, "retryDelayMs");
    ValidationUtils.requireNonEmpty(operationName, "operationName");

    Exception lastException = null;
    
    for (int attempt = 1; attempt <= maxRetries; attempt++) {
      try {
        log.debug("Executing {} (attempt {}/{})", operationName, attempt, maxRetries);
        T result = operation.execute();
        if (attempt > 1) {
          log.info("{} succeeded on attempt {}", operationName, attempt);
        }
        return result;
      } catch (Exception e) {
        lastException = e;
        log.warn("{} failed on attempt {}/{}: {}", 
            operationName, attempt, maxRetries, e.getMessage());
        
        if (attempt < maxRetries) {
          try {
            Thread.sleep(retryDelayMs);
          } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Retry interrupted", ie);
          }
        }
      }
    }
    
    log.error("{} failed after {} attempts", operationName, maxRetries);
    throw new RuntimeException(
        operationName + " failed after " + maxRetries + " attempts", lastException);
  }

  /**
   * Create a user-friendly error message from an exception.
   * REQ-ERR-003: Provide clear error messages without exposing internal details.
   * 
   * @param exception the exception to convert
   * @return user-friendly error message
   */
  public static String getUserFriendlyMessage(Exception exception) {
    if (exception instanceof IllegalArgumentException) {
      return "Invalid input: " + exception.getMessage();
    } else if (exception instanceof NullPointerException) {
      return "Required data is missing. Please check your request.";
    } else if (exception.getMessage() != null && 
               exception.getMessage().contains("connection")) {
      return "Service temporarily unavailable. Please try again later.";
    } else if (exception.getMessage() != null && 
               exception.getMessage().contains("timeout")) {
      return "Request timed out. Please try again.";
    } else {
      return "An unexpected error occurred. Please contact support if the issue persists.";
    }
  }

  /**
   * Create detailed error context for debugging.
   * 
   * @param methodName the method where error occurred
   * @param inputData the input data that caused the error
   * @return context map for logging
   */
  public static Map<String, Object> createErrorContext(String methodName, Object inputData) {
    Map<String, Object> context = new HashMap<>();
    context.put("method", methodName);
    context.put("inputData", inputData != null ? inputData.toString() : "null");
    context.put("timestamp", LocalDateTime.now());
    context.put("threadName", Thread.currentThread().getName());
    return context;
  }

  /**
   * Functional interface for retryable operations.
   */
  @FunctionalInterface
  public interface RetryableOperation<T> {
    T execute() throws Exception;
  }
}
