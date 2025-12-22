package com.vulinh.springcron;

import static org.junit.jupiter.api.Assertions.*;

import com.vulinh.springcron.data.*;
import java.time.DayOfWeek;
import java.time.Month;
import org.junit.jupiter.api.Test;

class SpringCronAdapterTest {

  @Test
  void testEveryNSecondsMinutesAdapter() {
    var input = SpringCronAdapter.everyNSecondsMinutes(13);

    assertEquals(SecondMinuteExpression.EVERY_N, input.expression());
    assertArrayEquals(new int[] {13}, input.arguments());
    assertEquals("*/13", input.toPartExpression());
  }

  @Test
  void testBetweenSecondsMinutesAdapter() {
    var input = SpringCronAdapter.betweenSecondsMinutes(35, 25);

    assertEquals(SecondMinuteExpression.BETWEEN, input.expression());
    assertArrayEquals(new int[] {35, 25}, input.arguments());
    assertEquals("25-35", input.toPartExpression());
  }

  @Test
  void testSpecificSecondsMinutesAdapter() {
    var input = SpringCronAdapter.specificSecondsMinutes(10, 20);

    assertEquals(SecondMinuteExpression.SPECIFIC_VALUES, input.expression());
    assertArrayEquals(new int[] {10, 20}, input.arguments());
    assertEquals("10,20", input.toPartExpression());
  }

  @Test
  void testSpecificSecondMinuteRangesAdapter() {
    var input =
        SpringCronAdapter.specificSecondMinuteRanges(
            Range.of(5, 10, RangeType.INFLEXIBLE), Range.of(7, 3, RangeType.INFLEXIBLE));

    assertEquals(SecondMinuteExpression.SPECIFIC_RANGES, input.expression());
    assertArrayEquals(new int[] {5, 10, 3, 7}, input.arguments());
    assertEquals("3-5,7-10", input.toPartExpression());
  }

  @Test
  void testEveryNHourAdapter() {
    var input = SpringCronAdapter.everyNHours(4);

    assertEquals(HourExpression.EVERY_N_HOUR, input.expression());
    assertArrayEquals(new int[] {4}, input.arguments());
    assertEquals("*/4", input.toPartExpression());
  }

  @Test
  void testBetweenHoursAdapter() {
    var input = SpringCronAdapter.betweenHours(5, 10);

    assertEquals(HourExpression.BETWEEN_HOURS, input.expression());
    assertArrayEquals(new int[] {5, 10}, input.arguments());
    assertEquals("5-10", input.toPartExpression());
  }

  @Test
  void testSpecificHoursAdapter() {
    var input = SpringCronAdapter.specificHours(2, 4, 6, 8, 10, 12, 12, 14, 16);

    assertEquals(HourExpression.SPECIFIC_HOURS, input.expression());
    assertArrayEquals(new int[] {2, 4, 6, 8, 10, 12, 12, 14, 16}, input.arguments());
    assertEquals("2,4,6,8,10,12,14,16", input.toPartExpression());
  }

  @Test
  void testSpecificHourRangesAdapter() {
    var input = SpringCronAdapter.specificHourRanges(Range.of(4, 2), Range.of(6, 22));

    assertEquals(HourExpression.SPECIFIC_HOUR_RANGES, input.expression());
    assertArrayEquals(new int[] {2, 4, 6, 22}, input.arguments());
    assertEquals("2-4,6-22", input.toPartExpression());
  }

  @Test
  void testEveryNDaysAdapter() {
    var input = SpringCronAdapter.everyNDays(14);

    assertEquals(DayExpression.EVERY_N_DAY, input.expression());
    assertArrayEquals(new int[] {14}, input.arguments());
    assertEquals("*/14", input.toPartExpression());
  }

  @Test
  void testBetweenDaysAdapter() {
    var input = SpringCronAdapter.betweenDays(1, 14);

    assertEquals(DayExpression.BETWEEN_DAYS, input.expression());
    assertArrayEquals(new int[] {1, 14}, input.arguments());
    assertEquals("1-14", input.toPartExpression());
  }

  @Test
  void testSpecificDayValuesAdapter() {
    var input = SpringCronAdapter.specificDays(2, 5, 10, 4, 2);

    assertEquals(DayExpression.SPECIFIC_DAYS, input.expression());
    assertArrayEquals(new int[] {2, 5, 10, 4, 2}, input.arguments()); // Duplication allowed
    assertEquals("2,5,10,4", input.toPartExpression());
  }

  @Test
  void testSpecificDayRangesAdapter() {
    var input =
        SpringCronAdapter.specificDayRanges(Range.of(1, 9), Range.of(10, 19), Range.of(31, 20));

    assertEquals(DayExpression.SPECIFIC_DAY_RANGES, input.expression());
    assertArrayEquals(new int[] {1, 9, 10, 19, 20, 31}, input.arguments());
    assertEquals("1-9,10-19,20-31", input.toPartExpression());
  }

  @Test
  void testNToLastDayOfMonthAdapter() {
    var input = SpringCronAdapter.nToLastDayOfMonth(5);

    assertEquals(DayExpression.N_TO_LAST_DAY, input.expression());
    assertArrayEquals(new int[] {5}, input.arguments());
    assertEquals("L-5", input.toPartExpression());
  }

  @Test
  void testEveryNMonthsAdapter() {
    var input = SpringCronAdapter.everyNMonths(2);

    assertEquals(MonthExpression.EVERY_N_MONTH, input.expression());
    assertArrayEquals(new int[] {2}, input.arguments());
    assertEquals("*/2", input.toPartExpression());
  }

  @Test
  void testBetweenMonthsAdapter() {
    var input = SpringCronAdapter.betweenMonths(Month.SEPTEMBER, Month.JUNE);

    assertEquals(MonthExpression.BETWEEN_MONTHS, input.expression());
    assertArrayEquals(new int[] {9, 6}, input.arguments());
    assertEquals("SEP-JUN", input.toPartExpression());
  }

  @Test
  void testSpecificMonthValuesAdapter() {
    var input =
        SpringCronAdapter.specificMonths(Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER);

    assertEquals(MonthExpression.SPECIFIC_MONTHS, input.expression());
    assertArrayEquals(new int[] {4, 6, 9, 11}, input.arguments());
    assertEquals("APR,JUN,SEP,NOV", input.toPartExpression());
  }

  @Test
  void testSpecificMonthRangesAdapter() {
    var input =
        SpringCronAdapter.specificMonthRanges(
            MonthRange.of(Month.JANUARY, Month.NOVEMBER), MonthRange.of(Month.JULY, Month.AUGUST));

    assertEquals(MonthExpression.SPECIFIC_MONTH_RANGES, input.expression());
    assertArrayEquals(new int[] {1, 11, 7, 8}, input.arguments());
    assertEquals("JAN-JUL,AUG-NOV", input.toPartExpression());
  }

  @Test
  void testEveryNWeekDaysAdapter() {
    var input = SpringCronAdapter.everyNWeekDays(5);

    assertEquals(WeekDayExpression.EVERY_N_WEEK_DAY, input.expression());
    assertArrayEquals(new int[] {5}, input.arguments());
    assertEquals("*/5", input.toPartExpression());
  }

  @Test
  void testBetweenWeekDaysAdapter() {
    var input = SpringCronAdapter.betweenWeekDays(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);

    assertEquals(WeekDayExpression.BETWEEN_WEEK_DAYS, input.expression());
    assertArrayEquals(new int[] {5, 6}, input.arguments());
    assertEquals("FRI-SAT", input.toPartExpression());
  }

  @Test
  void testSpecificWeekDayValuesAdapter() {
    var input = SpringCronAdapter.specificWeekDays(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY);

    assertEquals(WeekDayExpression.SPECIFIC_WEEK_DAYS, input.expression());
    assertArrayEquals(new int[] {7, 6}, input.arguments());
    assertEquals("SUN,SAT", input.toPartExpression());
  }

  @Test
  void testWeekDayNthOccurrenceAdapter() {
    var input = SpringCronAdapter.nthOccurrence(DayOfWeek.MONDAY, 2);

    assertEquals(WeekDayExpression.NTH_OCCURRENCE, input.expression());
    assertArrayEquals(new int[] {1, 2}, input.arguments());
    assertEquals("MON#2", input.toPartExpression());
  }

  @Test
  void testLastWeekDayOfMonthAdapter() {
    var input = SpringCronAdapter.lastWeekDayOfMonth(DayOfWeek.FRIDAY);

    assertEquals(WeekDayExpression.LAST_OF_MONTH, input.expression());
    assertArrayEquals(new int[] {5}, input.arguments());
    assertEquals("FRIL", input.toPartExpression());
  }
}
