package com.enterprise.rag.exception;

import com.enterprise.rag.util.ErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST API endpoints.
 * 
 * REQ-ERR-004: Provide consistent error responses across all endpoints
 * REQ-ERR-005: Log all exceptions with proper context
 * REQ-ERR-006: Return user-friendly error messages
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Handle validation exceptions from @Valid annotations.
   * REQ-ERR-004: Return validation errors with field-level details.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {
    
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Validation Failed")
        .message("Input validation failed. Please check the field errors.")
        .fieldErrors(errors)
        .build();

    log.warn("Validation exception: {}", errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Handle IllegalArgumentException for invalid inputs.
   * REQ-ERR-003: Convert IllegalArgumentException to user-friendly message.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Invalid Input")
        .message(ex.getMessage())
        .build();

    log.warn("Invalid argument: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Handle NullPointerException for missing required data.
   * REQ-ERR-006: Provide clear message for null pointer errors.
   */
  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<ErrorResponse> handleNullPointerException(
      NullPointerException ex) {
    
    String userMessage = ErrorHandler.getUserFriendlyMessage(ex);
    
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Missing Required Data")
        .message(userMessage)
        .build();

    ErrorHandler.logError("GlobalExceptionHandler.handleNullPointerException", 
        "Null pointer exception occurred", ex);
    
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Handle ResourceNotFoundException for 404 errors.
   * REQ-ERR-007: Return 404 for resource not found scenarios.
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error("Resource Not Found")
        .message(ex.getMessage())
        .build();

    log.warn("Resource not found: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  /**
   * Handle all other exceptions.
   * REQ-ERR-005: Log unexpected exceptions and return generic error message.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    
    String userMessage = ErrorHandler.getUserFriendlyMessage(ex);
    
    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error("Internal Server Error")
        .message(userMessage)
        .build();

    ErrorHandler.logError("GlobalExceptionHandler.handleGenericException", 
        "Unexpected exception occurred", ex);
    
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
