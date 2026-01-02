package com.vulinh.utils.springcron;

/**
 * Represents an interval with a start and end value.
 *
 * @param start start value
 * @param end end value
 */
public record Interval(int start, int end) {

  public static Interval of(int start, int end, IntervalType intervalType) {
    return intervalType == IntervalType.INFLEXIBLE
        ? Interval.of(Math.min(start, end), Math.max(start, end))
        : Interval.of(start, end);
  }

  public static Interval of(int start, int end) {
    return new Interval(start, end);
  }
}
