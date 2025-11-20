package com.enterprise.rag.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for time-related operations and conversions.
 * REQ-TIME-001: All time measurements must be consistent and well-documented.
 * 
 * Time Units Standards:
 * - Internal storage: milliseconds (ms) as Integer/Long
 * - Human-readable display: seconds with 2 decimal places
 * - Database: milliseconds (INTEGER type)
 * - API responses: ISO-8601 timestamps
 */
public final class TimeUtils {

  private TimeUtils() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Convert milliseconds to seconds with decimal precision.
   * REQ-TIME-002: Provide conversion utilities between time units.
   * 
   * @param milliseconds time in milliseconds (non-negative)
   * @return time in seconds with 2 decimal places
   * @throws IllegalArgumentException if milliseconds is negative
   */
  public static double millisecondsToSeconds(long milliseconds) {
    if (milliseconds < 0) {
      throw new IllegalArgumentException("Milliseconds cannot be negative: " + milliseconds);
    }
    return Math.round(milliseconds / 10.0) / 100.0; // 2 decimal precision
  }

  /**
   * Convert seconds to milliseconds.
   * REQ-TIME-002: Provide conversion utilities between time units.
   * 
   * @param seconds time in seconds (non-negative)
   * @return time in milliseconds
   * @throws IllegalArgumentException if seconds is negative
   */
  public static long secondsToMilliseconds(double seconds) {
    if (seconds < 0) {
      throw new IllegalArgumentException("Seconds cannot be negative: " + seconds);
    }
    return Math.round(seconds * 1000);
  }

  /**
   * Calculate execution time in milliseconds between two instants.
   * REQ-TIME-003: Provide accurate execution time measurement.
   * 
   * @param startTime start instant (must be before endTime)
   * @param endTime end instant (must be after startTime)
   * @return execution time in milliseconds
   * @throws IllegalArgumentException if startTime is after endTime or if either is null
   */
  public static Integer calculateExecutionTime(Instant startTime, Instant endTime) {
    if (startTime == null || endTime == null) {
      throw new IllegalArgumentException("Start and end times cannot be null");
    }
    if (startTime.isAfter(endTime)) {
      throw new IllegalArgumentException(
          "Start time must be before end time. Start: " + startTime + ", End: " + endTime);
    }
    return (int) Duration.between(startTime, endTime).toMillis();
  }

  /**
   * Calculate execution time in milliseconds from a start instant to now.
   * REQ-TIME-003: Provide accurate execution time measurement.
   * 
   * @param startTime start instant
   * @return execution time in milliseconds from start to now
   * @throws IllegalArgumentException if startTime is null or in the future
   */
  public static Integer calculateExecutionTimeFromNow(Instant startTime) {
    if (startTime == null) {
      throw new IllegalArgumentException("Start time cannot be null");
    }
    Instant now = Instant.now();
    if (startTime.isAfter(now)) {
      throw new IllegalArgumentException("Start time cannot be in the future: " + startTime);
    }
    return calculateExecutionTime(startTime, now);
  }

  /**
   * Check if execution time exceeds a threshold.
   * REQ-TIME-004: Support timeout validation.
   * 
   * @param executionTimeMs execution time in milliseconds
   * @param thresholdMs threshold in milliseconds
   * @return true if execution time exceeds threshold
   * @throws IllegalArgumentException if either value is negative
   */
  public static boolean exceedsThreshold(Integer executionTimeMs, Integer thresholdMs) {
    if (executionTimeMs == null || thresholdMs == null) {
      return false;
    }
    if (executionTimeMs < 0 || thresholdMs < 0) {
      throw new IllegalArgumentException("Time values cannot be negative");
    }
    return executionTimeMs > thresholdMs;
  }

  /**
   * Convert LocalDateTime to Instant for precise time calculations.
   * REQ-TIME-005: Support conversion between LocalDateTime and Instant.
   * 
   * @param localDateTime local date time to convert
   * @return instant representation
   * @throws IllegalArgumentException if localDateTime is null
   */
  public static Instant toInstant(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      throw new IllegalArgumentException("LocalDateTime cannot be null");
    }
    return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
  }

  /**
   * Convert Instant to LocalDateTime.
   * REQ-TIME-005: Support conversion between LocalDateTime and Instant.
   * 
   * @param instant instant to convert
   * @return local date time representation
   * @throws IllegalArgumentException if instant is null
   */
  public static LocalDateTime toLocalDateTime(Instant instant) {
    if (instant == null) {
      throw new IllegalArgumentException("Instant cannot be null");
    }
    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
  }

  /**
   * Format execution time for human-readable display.
   * REQ-TIME-006: Provide human-readable time formatting.
   * 
   * @param executionTimeMs execution time in milliseconds
   * @return formatted string (e.g., "1.23s", "123ms", "2m 5s")
   */
  public static String formatExecutionTime(Integer executionTimeMs) {
    if (executionTimeMs == null || executionTimeMs < 0) {
      return "N/A";
    }
    
    if (executionTimeMs < 1000) {
      return executionTimeMs + "ms";
    } else if (executionTimeMs < 60000) {
      return millisecondsToSeconds(executionTimeMs) + "s";
    } else {
      long minutes = executionTimeMs / 60000;
      long seconds = (executionTimeMs % 60000) / 1000;
      return minutes + "m " + seconds + "s";
    }
  }

  /**
   * Check if a time value is within valid bounds.
   * REQ-BOUND-001: Validate time values are within acceptable ranges.
   * 
   * @param timeMs time value in milliseconds
   * @param minMs minimum allowed value (inclusive)
   * @param maxMs maximum allowed value (inclusive)
   * @return true if within bounds
   */
  public static boolean isWithinBounds(Integer timeMs, int minMs, int maxMs) {
    if (timeMs == null) {
      return false;
    }
    return timeMs >= minMs && timeMs <= maxMs;
  }

  /**
   * Get current timestamp as milliseconds since epoch.
   * REQ-TIME-007: Provide consistent timestamp generation.
   * 
   * @return current time in milliseconds since epoch
   */
  public static long getCurrentTimestampMs() {
    return Instant.now().toEpochMilli();
  }

  /**
   * Calculate time elapsed since a given timestamp.
   * REQ-TIME-008: Calculate elapsed time from timestamp.
   * 
   * @param timestampMs timestamp in milliseconds since epoch
   * @return elapsed time in milliseconds
   * @throws IllegalArgumentException if timestamp is in the future
   */
  public static long getElapsedTimeMs(long timestampMs) {
    long currentMs = getCurrentTimestampMs();
    if (timestampMs > currentMs) {
      throw new IllegalArgumentException("Timestamp cannot be in the future");
    }
    return currentMs - timestampMs;
  }
}
