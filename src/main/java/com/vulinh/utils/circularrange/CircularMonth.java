package com.vulinh.utils.circularrange;

import java.time.Month;
import java.util.List;

/**
 * Circular range for Month (JANUARY to DECEMBER).
 *
 * @param start start month
 * @param end end month
 */
public record CircularMonth(Month start, Month end) implements CircularRange<Month> {

  /** All months in order. */
  static final List<Month> ALL = List.of(Month.values());

  /**
   * Factory method to create a {@link CircularMonth} instance.
   *
   * @param start start month
   * @param end end month
   * @return new {@link CircularMonth} instance
   */
  public static CircularMonth of(Month start, Month end) {
    return new CircularMonth(start, end);
  }

  @Override
  public List<Month> getAllElements() {
    return ALL;
  }
}
