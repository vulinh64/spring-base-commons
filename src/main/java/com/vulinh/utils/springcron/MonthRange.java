package com.vulinh.utils.springcron;

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
   * Convert to integer interval.
   *
   * @return The integer interval represent the month value.
   */
  public Interval toInterval() {
    return Interval.of(start().getValue(), end().getValue());
  }
}
