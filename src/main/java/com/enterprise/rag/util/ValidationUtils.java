package com.enterprise.rag.util;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility class for input validation and boundary condition handling.
 * REQ-VALID-001: All inputs must be validated before processing.
 * REQ-BOUND-002: Handle edge cases and boundary conditions gracefully.
 */
public final class ValidationUtils {

  private static final Pattern UUID_PATTERN = Pattern.compile(
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
  
  private static final int MAX_STRING_LENGTH = 10000;
  private static final int MAX_TOKEN_COUNT = 100000;
  private static final double MIN_TEMPERATURE = 0.0;
  private static final double MAX_TEMPERATURE = 2.0;

  private ValidationUtils() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Validate that a string is not null or empty.
   * REQ-VALID-002: Validate string inputs for null and empty values.
   * 
   * @param value string to validate
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if value is null or empty
   */
  public static void requireNonEmpty(String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException(fieldName + " cannot be null or empty");
    }
  }

  /**
   * Validate that an object is not null.
   * REQ-VALID-003: Validate object inputs for null values.
   * 
   * @param value object to validate
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if value is null
   */
  public static void requireNonNull(Object value, String fieldName) {
    if (value == null) {
      throw new IllegalArgumentException(fieldName + " cannot be null");
    }
  }

  /**
   * Validate that a collection is not null or empty.
   * REQ-BOUND-003: Validate collection boundaries.
   * 
   * @param collection collection to validate
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if collection is null or empty
   */
  public static void requireNonEmpty(Collection<?> collection, String fieldName) {
    if (collection == null || collection.isEmpty()) {
      throw new IllegalArgumentException(fieldName + " cannot be null or empty");
    }
  }

  /**
   * Validate that a map is not null or empty.
   * REQ-BOUND-003: Validate collection boundaries.
   * 
   * @param map map to validate
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if map is null or empty
   */
  public static void requireNonEmpty(Map<?, ?> map, String fieldName) {
    if (map == null || map.isEmpty()) {
      throw new IllegalArgumentException(fieldName + " cannot be null or empty");
    }
  }

  /**
   * Validate that an array is not null or empty.
   * REQ-BOUND-004: Validate array boundaries.
   * 
   * @param array array to validate
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if array is null or empty
   */
  public static void requireNonEmpty(Object[] array, String fieldName) {
    if (array == null || array.length == 0) {
      throw new IllegalArgumentException(fieldName + " cannot be null or empty");
    }
  }

  /**
   * Validate that a string length is within bounds.
   * REQ-BOUND-005: Validate string length boundaries.
   * 
   * @param value string to validate
   * @param maxLength maximum allowed length
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if string exceeds maxLength
   */
  public static void validateStringLength(String value, int maxLength, String fieldName) {
    if (value != null && value.length() > maxLength) {
      throw new IllegalArgumentException(
          fieldName + " exceeds maximum length of " + maxLength + " characters");
    }
  }

  /**
   * Validate that a numeric value is within a specified range.
   * REQ-BOUND-006: Validate numeric range boundaries.
   * 
   * @param value numeric value to validate
   * @param min minimum allowed value (inclusive)
   * @param max maximum allowed value (inclusive)
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if value is outside the range
   */
  public static void validateRange(int value, int min, int max, String fieldName) {
    if (value < min || value > max) {
      throw new IllegalArgumentException(
          fieldName + " must be between " + min + " and " + max + ", got: " + value);
    }
  }

  /**
   * Validate that a double value is within a specified range.
   * REQ-BOUND-006: Validate numeric range boundaries.
   * 
   * @param value double value to validate
   * @param min minimum allowed value (inclusive)
   * @param max maximum allowed value (inclusive)
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if value is outside the range
   */
  public static void validateRange(double value, double min, double max, String fieldName) {
    if (value < min || value > max) {
      throw new IllegalArgumentException(
          fieldName + " must be between " + min + " and " + max + ", got: " + value);
    }
  }

  /**
   * Validate that a value is positive.
   * REQ-BOUND-007: Validate positive numeric values.
   * 
   * @param value value to validate
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if value is not positive
   */
  public static void requirePositive(int value, String fieldName) {
    if (value <= 0) {
      throw new IllegalArgumentException(fieldName + " must be positive, got: " + value);
    }
  }

  /**
   * Validate that a value is non-negative.
   * REQ-BOUND-008: Validate non-negative numeric values.
   * 
   * @param value value to validate
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if value is negative
   */
  public static void requireNonNegative(int value, String fieldName) {
    if (value < 0) {
      throw new IllegalArgumentException(fieldName + " cannot be negative, got: " + value);
    }
  }

  /**
   * Validate that a string is a valid UUID.
   * REQ-VALID-004: Validate UUID format.
   * 
   * @param value string to validate
   * @param fieldName name of the field for error messages
   * @return parsed UUID
   * @throws IllegalArgumentException if value is not a valid UUID
   */
  public static UUID validateAndParseUUID(String value, String fieldName) {
    requireNonEmpty(value, fieldName);
    if (!UUID_PATTERN.matcher(value).matches()) {
      throw new IllegalArgumentException(fieldName + " is not a valid UUID: " + value);
    }
    try {
      return UUID.fromString(value);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(fieldName + " is not a valid UUID: " + value, e);
    }
  }

  /**
   * Validate token count is within acceptable range.
   * REQ-BOUND-009: Validate token count boundaries.
   * 
   * @param tokenCount number of tokens
   * @throws IllegalArgumentException if token count exceeds maximum
   */
  public static void validateTokenCount(Integer tokenCount) {
    if (tokenCount != null) {
      requireNonNegative(tokenCount, "Token count");
      if (tokenCount > MAX_TOKEN_COUNT) {
        throw new IllegalArgumentException(
            "Token count exceeds maximum of " + MAX_TOKEN_COUNT + ": " + tokenCount);
      }
    }
  }

  /**
   * Validate temperature parameter for LLM requests.
   * REQ-VALID-005: Validate LLM parameters.
   * 
   * @param temperature temperature value
   * @throws IllegalArgumentException if temperature is out of range
   */
  public static void validateTemperature(Double temperature) {
    if (temperature != null) {
      validateRange(temperature, MIN_TEMPERATURE, MAX_TEMPERATURE, "Temperature");
    }
  }

  /**
   * Validate query string for chat requests.
   * REQ-VALID-006: Validate chat query inputs.
   * 
   * @param query query string
   * @throws IllegalArgumentException if query is invalid
   */
  public static void validateQuery(String query) {
    requireNonEmpty(query, "Query");
    validateStringLength(query, MAX_STRING_LENGTH, "Query");
  }

  /**
   * Validate session ID format.
   * REQ-VALID-007: Validate session identifier format.
   * 
   * @param sessionId session identifier
   * @return parsed UUID
   * @throws IllegalArgumentException if session ID is invalid
   */
  public static UUID validateSessionId(String sessionId) {
    return validateAndParseUUID(sessionId, "Session ID");
  }

  /**
   * Validate collection size is within bounds.
   * REQ-BOUND-010: Validate collection size boundaries.
   * 
   * @param collection collection to validate
   * @param maxSize maximum allowed size
   * @param fieldName name of the field for error messages
   * @throws IllegalArgumentException if collection exceeds maxSize
   */
  public static void validateCollectionSize(Collection<?> collection, int maxSize, 
                                           String fieldName) {
    if (collection != null && collection.size() > maxSize) {
      throw new IllegalArgumentException(
          fieldName + " exceeds maximum size of " + maxSize + ": " + collection.size());
    }
  }

  /**
   * Safely get value from collection at index or return null.
   * REQ-BOUND-011: Safe collection access with boundary checking.
   * 
   * @param list list to access
   * @param index index to retrieve
   * @param <T> type of elements
   * @return element at index or null if out of bounds
   */
  public static <T> T safeGet(java.util.List<T> list, int index) {
    if (list == null || index < 0 || index >= list.size()) {
      return null;
    }
    return list.get(index);
  }
}
