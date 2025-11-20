package com.enterprise.rag.util;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * Utility class for input validation and boundary condition checking.
 * 
 * REQ-001: Validate all user inputs for null, empty, and boundary conditions
 * REQ-002: Provide meaningful error messages for invalid inputs
 */
public final class ValidationUtils {

  private static final int MAX_STRING_LENGTH = 10000;
  private static final int MIN_TEMPERATURE = 0;
  private static final int MAX_TEMPERATURE = 2;
  private static final int MIN_TOKEN_COUNT = 1;
  private static final int MAX_TOKEN_COUNT = 32000;

  private ValidationUtils() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Validates that an object is not null.
   * 
   * @param obj the object to validate
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the object is null
   */
  public static void requireNonNull(Object obj, String fieldName) {
    if (obj == null) {
      throw new IllegalArgumentException(fieldName + " cannot be null");
    }
  }

  /**
   * Validates that a string is not null or empty.
   * 
   * @param str the string to validate
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the string is null or empty
   */
  public static void requireNonEmpty(String str, String fieldName) {
    requireNonNull(str, fieldName);
    if (str.trim().isEmpty()) {
      throw new IllegalArgumentException(fieldName + " cannot be empty");
    }
  }

  /**
   * Validates that a string length is within acceptable bounds.
   * 
   * @param str the string to validate
   * @param fieldName the name of the field being validated
   * @param maxLength the maximum allowed length
   * @throws IllegalArgumentException if the string exceeds max length
   */
  public static void validateStringLength(String str, String fieldName, int maxLength) {
    requireNonNull(str, fieldName);
    if (str.length() > maxLength) {
      throw new IllegalArgumentException(
          fieldName + " exceeds maximum length of " + maxLength + " characters");
    }
  }

  /**
   * Validates that a string length is within default acceptable bounds.
   * 
   * @param str the string to validate
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the string exceeds default max length
   */
  public static void validateStringLength(String str, String fieldName) {
    validateStringLength(str, fieldName, MAX_STRING_LENGTH);
  }

  /**
   * Validates that a collection is not null or empty.
   * 
   * @param collection the collection to validate
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the collection is null or empty
   */
  public static void requireNonEmpty(Collection<?> collection, String fieldName) {
    requireNonNull(collection, fieldName);
    if (collection.isEmpty()) {
      throw new IllegalArgumentException(fieldName + " cannot be empty");
    }
  }

  /**
   * Validates that a map is not null or empty.
   * 
   * @param map the map to validate
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the map is null or empty
   */
  public static void requireNonEmpty(Map<?, ?> map, String fieldName) {
    requireNonNull(map, fieldName);
    if (map.isEmpty()) {
      throw new IllegalArgumentException(fieldName + " cannot be empty");
    }
  }

  /**
   * Validates that a number is within a specified range (inclusive).
   * 
   * @param value the value to validate
   * @param min the minimum allowed value
   * @param max the maximum allowed value
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the value is out of range
   */
  public static void validateRange(int value, int min, int max, String fieldName) {
    if (value < min || value > max) {
      throw new IllegalArgumentException(
          fieldName + " must be between " + min + " and " + max + ", but was: " + value);
    }
  }

  /**
   * Validates that a double value is within a specified range (inclusive).
   * 
   * @param value the value to validate
   * @param min the minimum allowed value
   * @param max the maximum allowed value
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the value is out of range
   */
  public static void validateRange(double value, double min, double max, String fieldName) {
    if (value < min || value > max) {
      throw new IllegalArgumentException(
          fieldName + " must be between " + min + " and " + max + ", but was: " + value);
    }
  }

  /**
   * Validates that a temperature value is within acceptable bounds.
   * REQ-003: Temperature must be between 0.0 and 2.0
   * 
   * @param temperature the temperature to validate
   * @throws IllegalArgumentException if temperature is out of bounds
   */
  public static void validateTemperature(Double temperature) {
    if (temperature != null) {
      validateRange(temperature, MIN_TEMPERATURE, MAX_TEMPERATURE, "temperature");
    }
  }

  /**
   * Validates that a token count is within acceptable bounds.
   * REQ-004: Token count must be between 1 and 32000
   * 
   * @param tokenCount the token count to validate
   * @throws IllegalArgumentException if token count is out of bounds
   */
  public static void validateTokenCount(Integer tokenCount) {
    if (tokenCount != null) {
      validateRange(tokenCount, MIN_TOKEN_COUNT, MAX_TOKEN_COUNT, "tokenCount");
    }
  }

  /**
   * Validates that a string is a valid UUID format.
   * 
   * @param uuidStr the UUID string to validate
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the string is not a valid UUID
   */
  public static void validateUuid(String uuidStr, String fieldName) {
    requireNonEmpty(uuidStr, fieldName);
    try {
      UUID.fromString(uuidStr);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(fieldName + " must be a valid UUID format", e);
    }
  }

  /**
   * Validates an email format (basic validation).
   * 
   * @param email the email to validate
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the email format is invalid
   */
  public static void validateEmail(String email, String fieldName) {
    requireNonEmpty(email, fieldName);
    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      throw new IllegalArgumentException(fieldName + " must be a valid email format");
    }
  }

  /**
   * Validates that a value is positive.
   * 
   * @param value the value to validate
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the value is not positive
   */
  public static void requirePositive(int value, String fieldName) {
    if (value <= 0) {
      throw new IllegalArgumentException(fieldName + " must be positive, but was: " + value);
    }
  }

  /**
   * Validates that a value is non-negative.
   * 
   * @param value the value to validate
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the value is negative
   */
  public static void requireNonNegative(int value, String fieldName) {
    if (value < 0) {
      throw new IllegalArgumentException(fieldName + " must be non-negative, but was: " + value);
    }
  }

  /**
   * Validates that a value is non-negative.
   * 
   * @param value the value to validate
   * @param fieldName the name of the field being validated
   * @throws IllegalArgumentException if the value is negative
   */
  public static void requireNonNegative(long value, String fieldName) {
    if (value < 0) {
      throw new IllegalArgumentException(fieldName + " must be non-negative, but was: " + value);
    }
  }
}
