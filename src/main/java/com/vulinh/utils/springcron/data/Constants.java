package com.vulinh.utils.springcron.data;

import com.vulinh.utils.CommonUtils;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A utility class containing constant values and mappings used in cron expression generations and
 * validations.
 */
class Constants {

  /** Value 0 */
  static final int ZERO = 0;

  /** Value 1 */
  static final int ONE = 1;

  /** The minimum value for seconds and minutes in cron expressions. */
  static final int SECOND_MINUTE_ONE = ONE;

  /** The maximum value for seconds and minutes in cron expressions. */
  static final int SECOND_MINUTE_MAX = 59;

  /** The minimum value for hours in cron expressions. */
  static final int HOUR_MIN = ZERO;

  /** The minimum value for hours in cron expressions. */
  static final int HOUR_ONE = ONE;

  /** The maximum value for hours in cron expressions. */
  static final int HOUR_MAX = 23;

  /** The minimum value for days of the month in cron expressions. */
  static final int DAY_OF_MONTH_MIN = ONE;

  /** The maximum value for days of the month in cron expressions. */
  static final int DAY_OF_MONTH_MAX = 31;

  /** The minimum value for months in cron expressions */
  static final int MONTH_MIN = ONE;

  /** The maximum value for months in cron expressions */
  static final int MONTH_MAX = 12;

  /** The minimum value for day of week in cron expressions. 0 and 7 can both represent Sunday. */
  static final int DAY_OF_WEEK_MIN = ZERO;

  /** The maximum value for day of week in cron expressions. 0 and 7 can both represent Sunday. */
  static final int DAY_OF_WEEK_MAX = 7;

  /** The minimum occurrence for a day of week in cron expressions */
  static final int MIN_NTH_OCCURRENCE = ONE;

  /** The maximum occurrence for a day of week in cron expressions */
  static final int MAX_NTH_OCCURRENCE = 5;

  /** A mapping of month numbers to their three-letter abbreviations. */
  static final Map<Integer, String> MONTH_MAP =
      Map.ofEntries(
              Map.entry(1, Month.JANUARY),
              Map.entry(2, Month.FEBRUARY),
              Map.entry(3, Month.MARCH),
              Map.entry(4, Month.APRIL),
              Map.entry(5, Month.MAY),
              Map.entry(6, Month.JUNE),
              Map.entry(7, Month.JULY),
              Map.entry(8, Month.AUGUST),
              Map.entry(9, Month.SEPTEMBER),
              Map.entry(10, Month.OCTOBER),
              Map.entry(11, Month.NOVEMBER),
              Map.entry(12, Month.DECEMBER))
          .entrySet()
          .stream()
          .collect(
              Collectors.toMap(Map.Entry::getKey, v -> CommonUtils.first3Letters(v.getValue())));

  /**
   * A mapping of day of week numbers to their three-letter abbreviations. Note that both 0 and 7
   * map to SUNDAY.
   */
  static final Map<Integer, String> DAY_OF_WEEK_MAP =
      Map.ofEntries(
              Map.entry(0, DayOfWeek.SUNDAY),
              Map.entry(1, DayOfWeek.MONDAY),
              Map.entry(2, DayOfWeek.TUESDAY),
              Map.entry(3, DayOfWeek.WEDNESDAY),
              Map.entry(4, DayOfWeek.THURSDAY),
              Map.entry(5, DayOfWeek.FRIDAY),
              Map.entry(6, DayOfWeek.SATURDAY),
              Map.entry(7, DayOfWeek.SUNDAY))
          .entrySet()
          .stream()
          .collect(
              Collectors.toMap(Map.Entry::getKey, v -> CommonUtils.first3Letters(v.getValue())));

  /** No instantiation. */
  private Constants() {}
}
