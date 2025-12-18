package com.vulinh.springcron;

import java.time.Month;

/**
 * Convenient range of months for usage in {@link SpringCronAdapter}. No need to be in ascending
 * order.
 *
 * @param start Start month.
 * @param end End month.
 */
public record MonthRange(Month start, Month end) {

  public static MonthRange of(Month start, Month end) {
    return new MonthRange(start, end);
  }

  /**
   * Convert to integer range.
   *
   * @return The integer range represent the month value.
   */
  public Range toRange() {
    return new Range(start().getValue(), end().getValue());
  }
}
