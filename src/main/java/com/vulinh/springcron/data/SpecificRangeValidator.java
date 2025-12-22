package com.vulinh.springcron.data;

import java.util.Collections;
import java.util.List;

/**
 * Defines specific ranges for various time units used in cron expressions, along with validation
 *
 * @param minimum The minimum value of the range.
 * @param maximum The maximum value of the range.
 */
record SpecificRangeValidator(int minimum, int maximum) {

  /** The single instance of specific ranges for minutes and seconds in cron expressions. */
  static final SpecificRangeValidator MINUTE_SECOND_RANGE_VALIDATOR =
      new SpecificRangeValidator(Constants.ZERO, Constants.SECOND_MINUTE_MAX);

  /** The single instance of specific ranges for hours in cron expressions. */
  static final SpecificRangeValidator HOUR_RANGE_VALIDATOR =
      new SpecificRangeValidator(Constants.HOUR_MIN, Constants.HOUR_MAX);

  /** The single instance of specific ranges for days of the month in cron expressions. */
  static final SpecificRangeValidator DAY_OF_MONTH_RANGE_VALIDATOR =
      new SpecificRangeValidator(Constants.DAY_OF_MONTH_MIN, Constants.DAY_OF_MONTH_MAX);

  /** The single instance of specific ranges for months in cron expressions. */
  static final SpecificRangeValidator MONTH_RANGE_VALIDATOR =
      new SpecificRangeValidator(Constants.MONTH_MIN, Constants.MONTH_MAX);

  /**
   * Creates a predicate to validate a list of values for specific ranges.
   *
   * @return A predicate that checks if the list of values is valid for the specific range.
   */
  public boolean isValidMultiRangesList(List<Integer> list) {
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
