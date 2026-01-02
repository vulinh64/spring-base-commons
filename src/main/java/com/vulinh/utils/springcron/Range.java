package com.vulinh.utils.springcron;

/**
 * Represents a range with a start and end value.
 *
 * @param start start value
 * @param end end value
 */
public record Range(int start, int end) {

  public static Range of(int start, int end, RangeType rangeType) {
    return rangeType == RangeType.INFLEXIBLE
        ? Range.of(Math.min(start, end), Math.max(start, end))
        : Range.of(start, end);
  }

  public static Range of(int start, int end) {
    return new Range(start, end);
  }
}
