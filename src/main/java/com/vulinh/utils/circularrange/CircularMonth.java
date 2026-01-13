package com.vulinh.utils.circularrange;

import com.vulinh.utils.CommonUtils;
import java.time.Month;
import java.util.List;
import java.util.function.Function;

/**
 * Circular range for Month (JANUARY to DECEMBER).
 *
 * <p>This class represents a circular range of months, allowing for ranges that may wrap around the
 * end of the year (e.g., DECEMBER to FEBRUARY). It implements the {@link CircularRange} interface
 * for {@link Month} values, providing a fixed cycle of all months in order from JANUARY to
 * DECEMBER.
 *
 * <p>Use {@link #of(Month, Month)} to create instances.
 *
 * <pre>
 *   CircularMonth range = CircularMonth.of(Month.DECEMBER, Month.FEBRUARY);
 * </pre>
 *
 * @param start the start month (inclusive)
 * @param end the end month (inclusive)
 */
public record CircularMonth(Month start, Month end) implements CircularRange<Month> {

  /** All months in order. */
  static final List<Month> ALL = List.of(Month.values());

  /**
   * Returns a new {@link CircularMonth} instance for the given start and end months.
   *
   * @param start the start month (inclusive)
   * @param end the end month (inclusive)
   * @return a new {@link CircularMonth} instance
   */
  public static CircularMonth of(Month start, Month end) {
    return new CircularMonth(start, end);
  }

  @Override
  public List<Month> getAllElements() {
    return ALL;
  }

  @Override
  public Function<Month, String> getTransformer() {
    return CommonUtils::first3Letters;
  }
}
