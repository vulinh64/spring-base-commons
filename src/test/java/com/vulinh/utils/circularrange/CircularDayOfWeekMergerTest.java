package com.vulinh.utils.circularrange;

import static com.vulinh.utils.circularrange.Merger.mergeCircularRanges;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CircularDayOfWeekMergerTest {

  static Stream<Arguments> provideRanges() {
    return Stream.of(
        of(List.of(), "", false),
        of(null, "", false),
        of(
            List.of(CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)),
            "MONDAY-WEDNESDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                CircularDayOfWeek.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY)),
            "MONDAY-THURSDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
                CircularDayOfWeek.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY)),
            "MONDAY-WEDNESDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
                CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)),
            "MONDAY-TUESDAY,THURSDAY-FRIDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)),
            "MONDAY-TUESDAY,THURSDAY-SATURDAY",
            false),
        of(
            List.of(CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.MONDAY)),
            "SATURDAY-MONDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.MONDAY),
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)),
            "SATURDAY-TUESDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.MONDAY),
                CircularDayOfWeek.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY)),
            "WEDNESDAY-THURSDAY,SATURDAY-MONDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.FRIDAY, DayOfWeek.MONDAY),
                CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.TUESDAY)),
            "FRIDAY-TUESDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                CircularDayOfWeek.of(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY),
                CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.SATURDAY)),
            "MONDAY-SATURDAY",
            false),
        of(List.of(CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.SUNDAY)), "*", false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
                CircularDayOfWeek.of(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY)),
            "*",
            false),
        of(List.of(CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.WEDNESDAY)), "*", false),
        of(
            List.of(CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY)),
            "MONDAY-MONDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.SATURDAY),
                CircularDayOfWeek.of(DayOfWeek.SUNDAY, DayOfWeek.SUNDAY)),
            "*",
            false),
        of(
            List.of(CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)),
            "SATURDAY-SUNDAY",
            false),
        of(
            List.of(CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)),
            "MONDAY-FRIDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)),
            "*",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY),
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY)),
            "FRIDAY-TUESDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
                CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)),
            "THURSDAY-TUESDAY",
            false),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
                CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                CircularDayOfWeek.of(DayOfWeek.SUNDAY, DayOfWeek.SUNDAY)),
            "THURSDAY-FRIDAY,SUNDAY-TUESDAY",
            false));
  }

  static Stream<Arguments> provideRangesWithGap() {
    return Stream.of(
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY),
                CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)),
            "MONDAY-FRIDAY,SATURDAY-SUNDAY",
            true),
        of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
                CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)),
            "MONDAY-TUESDAY,THURSDAY-FRIDAY,SATURDAY-SUNDAY",
            true));
  }

  @ParameterizedTest
  @MethodSource("provideRanges")
  void testMergeCircularRanges(List<CircularDayOfWeek> ranges, String expected) {
    assertEquals(expected, mergeCircularRanges(ranges));
  }

  @ParameterizedTest
  @MethodSource("provideRangesWithGap")
  void testMergeCircularRangesWithGap(
      List<CircularDayOfWeek> ranges, String expected, boolean allowGap) {
    assertEquals(
        expected, mergeCircularRanges(ranges, allowGap ? MergeMode.ALLOW_GAPS : MergeMode.NO_GAPS));
  }
}
