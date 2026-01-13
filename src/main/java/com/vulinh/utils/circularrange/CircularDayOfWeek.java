package com.vulinh.utils.circularrange;

import com.vulinh.utils.CommonUtils;

import java.time.DayOfWeek;
import java.util.List;
import java.util.function.Function;

/**
 * Circular range for DayOfWeek (MONDAY to SUNDAY).
 *
 * @param start start day of week
 * @param end end day of week
 */
public record CircularDayOfWeek(DayOfWeek start, DayOfWeek end)
    implements CircularRange<DayOfWeek> {

  /** All days of the week in order. */
  static final List<DayOfWeek> ALL = List.of(DayOfWeek.values());

  /**
   * Factory method to create a {@link CircularDayOfWeek} instance.
   *
   * @param start start day of week
   * @param end end day of week
   * @return new {@link CircularDayOfWeek} instance
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
