package com.vulinh.utils.springcron.data;

import java.util.Collections;
import java.util.List;

/**
 * Defines specific intervals for various time units used in cron expressions, along with
 * validation.
 *
 * @param minimum The minimum value of the interval.
 * @param maximum The maximum value of the interval.
 */
record SpecificIntervalValidator(int minimum, int maximum) {

  /** The single instance of specific intervals for minutes and seconds in cron expressions. */
  static final SpecificIntervalValidator MINUTE_SECOND_INTERVAL_VALIDATOR =
      new SpecificIntervalValidator(Constants.ZERO, Constants.SECOND_MINUTE_MAX);

  /** The single instance of specific intervals for hours in cron expressions. */
  static final SpecificIntervalValidator HOUR_INTERVAL_VALIDATOR =
      new SpecificIntervalValidator(Constants.HOUR_MIN, Constants.HOUR_MAX);

  /** The single instance of specific intervals for days of the month in cron expressions. */
  static final SpecificIntervalValidator DAY_OF_MONTH_INTERVAL_VALIDATOR =
      new SpecificIntervalValidator(Constants.DAY_OF_MONTH_MIN, Constants.DAY_OF_MONTH_MAX);

  /** The single instance of specific intervals for months in cron expressions. */
  static final SpecificIntervalValidator MONTH_INTERVAL_VALIDATOR =
      new SpecificIntervalValidator(Constants.MONTH_MIN, Constants.MONTH_MAX);

  /**
   * Creates a predicate to validate a list of values for specific intervals.
   *
   * @return A predicate that checks if the list of values is valid for the specific interval.
   */
  public boolean isValidMultiIntervalList(List<Integer> list) {
    // Must be at least two elements and even number of elements
    if (list.size() < 2 || (list.size() & 1) != 0) {
      return false;
    }

    for (var e : list) {
      if (e.compareTo(minimum()) < 0 || e.compareTo(maximum()) > 0) {
        return false;
      }

      // Cannot appear more than twice in a list
      if (Collections.frequency(list, e) > 2) {
        return false;
      }
    }

    return true;
  }
}
