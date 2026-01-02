package com.vulinh.utils.springcron;

import com.vulinh.utils.springcron.data.*;
import com.vulinh.utils.springcron.input.*;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.Arrays;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

/** Convenient methods to easily convert to Spring Cron input objects. */
public class SpringCronAdapter {

  private SpringCronAdapter() {}

  public static SecondMinuteCronInput everyNSecondsMinutes(int n) {
    return everyN(SecondMinuteExpression.EVERY_N, SecondMinuteCronInput.builder(), n);
  }

  public static SecondMinuteCronInput betweenSecondsMinutes(int from, int to) {
    return between(SecondMinuteExpression.BETWEEN, SecondMinuteCronInput.builder(), from, to);
  }

  public static SecondMinuteCronInput specificSecondsMinutes(int... values) {
    return specificValues(
        SecondMinuteExpression.SPECIFIC_VALUES, SecondMinuteCronInput.builder(), values);
  }

  public static SecondMinuteCronInput specificSecondMinuteIntervals(Interval... intervals) {
    return specificIntervals(
        SecondMinuteExpression.SPECIFIC_INTERVALS,
        SecondMinuteCronInput.builder(),
        IntervalType.INFLEXIBLE,
            intervals);
  }

  public static HourCronInput everyNHours(int n) {
    return SpringCronAdapter.everyN(HourExpression.EVERY_N_HOUR, HourCronInput.builder(), n);
  }

  public static HourCronInput betweenHours(int from, int to) {
    return between(HourExpression.BETWEEN_HOURS, HourCronInput.builder(), from, to);
  }

  public static HourCronInput specificHours(int... hours) {
    return specificValues(HourExpression.SPECIFIC_HOURS, HourCronInput.builder(), hours);
  }

  public static HourCronInput specificHourRanges(Interval... intervals) {
    return specificIntervals(
        HourExpression.SPECIFIC_HOUR_INTERVALS, HourCronInput.builder(), IntervalType.INFLEXIBLE, intervals);
  }

  public static DayCronInput everyNDays(int n) {
    return everyN(DayExpression.EVERY_N_DAY, DayCronInput.builder(), n);
  }

  public static DayCronInput betweenDays(int from, int to) {
    return between(DayExpression.BETWEEN_DAYS, DayCronInput.builder(), from, to);
  }

  public static DayCronInput specificDays(int... days) {
    return specificValues(DayExpression.SPECIFIC_DAYS, DayCronInput.builder(), days);
  }

  public static DayCronInput specificDayRanges(Interval... intervals) {
    return specificIntervals(
        DayExpression.SPECIFIC_DAY_INTERVALS, DayCronInput.builder(), IntervalType.INFLEXIBLE, intervals);
  }

  public static DayCronInput nToLastDayOfMonth(int n) {
    return DayCronInput.builder().expression(DayExpression.N_TO_LAST_DAY).arguments(n).build();
  }

  public static MonthCronInput everyNMonths(int n) {
    return everyN(MonthExpression.EVERY_N_MONTH, MonthCronInput.builder(), n);
  }

  public static MonthCronInput betweenMonths(Month from, Month to) {
    return between(
        MonthExpression.BETWEEN_MONTHS, MonthCronInput.builder(), from.getValue(), to.getValue());
  }

  public static MonthCronInput specificMonths(Month... months) {
    return specificValues(
        MonthExpression.SPECIFIC_MONTHS,
        MonthCronInput.builder(),
        toIntArray(Month::getValue, months));
  }

  public static MonthCronInput specificMonthRanges(MonthRange... monthRanges) {
    return specificIntervals(
        MonthExpression.SPECIFIC_MONTH_INTERVAL,
        MonthCronInput.builder(),
        IntervalType.FLEXIBLE,
        Arrays.stream(monthRanges).map(MonthRange::toInterval).toArray(Interval[]::new));
  }

  public static WeekDayCronInput everyNWeekDays(int n) {
    return everyN(WeekDayExpression.EVERY_N_WEEK_DAY, WeekDayCronInput.builder(), n);
  }

  public static WeekDayCronInput betweenWeekDays(DayOfWeek from, DayOfWeek to) {
    return between(
        WeekDayExpression.BETWEEN_WEEK_DAYS,
        WeekDayCronInput.builder(),
        from.getValue(),
        to.getValue());
  }

  public static WeekDayCronInput specificWeekDays(DayOfWeek... weekDays) {
    return specificValues(
        WeekDayExpression.SPECIFIC_WEEK_DAYS,
        WeekDayCronInput.builder(),
        toIntArray(DayOfWeek::getValue, weekDays));
  }

  public static WeekDayCronInput nthOccurrence(DayOfWeek weekDay, int nth) {
    return WeekDayCronInput.builder()
        .expression(WeekDayExpression.NTH_OCCURRENCE)
        .arguments(weekDay.getValue(), nth)
        .build();
  }

  public static WeekDayCronInput lastWeekDayOfMonth(DayOfWeek weekDay) {
    return WeekDayCronInput.builder()
        .expression(WeekDayExpression.LAST_OF_MONTH)
        .arguments(weekDay.getValue())
        .build();
  }

  private static <P extends PartExpression, C extends CronInput<P>> C everyN(
      P expression, CronInputBuilder<P, C> builder, int arguments) {
    return builder.expression(expression).arguments(arguments).build();
  }

  private static <P extends PartExpression, C extends CronInput<P>> C between(
      P expression, CronInputBuilder<P, C> builder, int from, int to) {
    return builder.expression(expression).arguments(from, to).build();
  }

  private static <P extends PartExpression, C extends CronInput<P>> C specificValues(
      P expression, CronInputBuilder<P, C> builder, int... values) {
    return builder.expression(expression).arguments(values).build();
  }

  private static <P extends PartExpression, C extends CronInput<P>> C specificIntervals(
          P expression, CronInputBuilder<P, C> builder, IntervalType intervalType, Interval... intervals) {
    return builder
        .expression(expression)
        .arguments(
            Arrays.stream(intervals)
                .map(s -> Interval.of(s.start(), s.end(), intervalType))
                .flatMap(s -> Stream.of(s.start(), s.end()))
                .mapToInt(Integer::intValue)
                .toArray())
        .build();
  }

  @SafeVarargs
  private static <T> int[] toIntArray(ToIntFunction<T> intMapper, T... values) {
    return Arrays.stream(values).mapToInt(intMapper).toArray();
  }
}
