package com.vulinh.utils.circularrange;

import com.vulinh.utils.CommonUtils;

import java.time.DayOfWeek;
import java.util.List;
import java.util.function.Function;

/**
 * Circular range for DayOfWeek (MONDAY to SUNDAY).
 * <p>
 * This class represents a circular range of days of the week, allowing for ranges that may wrap around
 * the end of the week (e.g., FRIDAY to MONDAY). It implements the {@link CircularRange} interface for
 * {@link DayOfWeek} values, providing a fixed cycle of all days in order from MONDAY to SUNDAY.
 * <p>
 * Use {@link #of(DayOfWeek, DayOfWeek)} to create instances.
 *
 * <pre>
 *   CircularDayOfWeek range = CircularDayOfWeek.of(DayOfWeek.FRIDAY, DayOfWeek.MONDAY);
 * </pre>
 *
 * @param start the start day of week (inclusive)
 * @param end the end day of week (inclusive)
 */
public record CircularDayOfWeek(DayOfWeek start, DayOfWeek end)
    implements CircularRange<DayOfWeek> {

  /** All days of the week in order. */
  static final List<DayOfWeek> ALL = List.of(DayOfWeek.values());

  /**
   * Returns a new {@link CircularDayOfWeek} instance for the given start and end days.
   *
   * @param start the start day of week (inclusive)
   * @param end the end day of week (inclusive)
   * @return a new {@link CircularDayOfWeek} instance
   */
  public static CircularDayOfWeek of(DayOfWeek start, DayOfWeek end) {
    return new CircularDayOfWeek(start, end);
  }

  @Override
  public List<DayOfWeek> getAllElements() {
    return ALL;
  }

  @Override
  public Function<DayOfWeek, String> getTransformer() {
    return CommonUtils::first3Letters;
  }
}
