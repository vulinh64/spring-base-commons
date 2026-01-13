package com.vulinh.utils.circularrange;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MergerDayOfWeekTest {

  @ParameterizedTest
  @MethodSource("emptyOrNullProvider")
  void testMergeCircularRangesEmptyOrNull(List<CircularDayOfWeek> input) {
    assertTrue(Merger.mergeCircularRanges(input).isEmpty());
  }

  @ParameterizedTest
  @MethodSource("singleRangeProvider")
  void testMergeCircularRangesSingleRange(
      List<CircularDayOfWeek> input, int expectedSize, String expectedStart, String expectedEnd) {
    var result = Merger.mergeCircularRanges(input);

    assertEquals(expectedSize, result.size());

    var first = result.get(0);

    assertEquals(expectedStart, first.start());
    assertEquals(expectedEnd, first.end());
  }

  @ParameterizedTest
  @MethodSource("mergeToOneProvider")
  void testMergeCircularRangesMergeToOne(
      List<CircularDayOfWeek> input, String expectedStart, String expectedEnd) {
    var result = Merger.mergeCircularRanges(input);

    assertEquals(1, result.size());

    var first = result.get(0);

    assertEquals(expectedStart, first.start());
    assertEquals(expectedEnd, first.end());
  }

  @ParameterizedTest
  @MethodSource("mergeToTwoProvider")
  void testMergeCircularRangesMergeToTwo(
      List<CircularDayOfWeek> input,
      String expectedStart1,
      String expectedEnd1,
      String expectedStart2,
      String expectedEnd2) {
    var result = Merger.mergeCircularRanges(input);

    assertEquals(2, result.size());

    var first = result.get(0);
    var second = result.get(1);

    assertEquals(expectedStart1, first.start());
    assertEquals(expectedEnd1, first.end());
    assertEquals(expectedStart2, second.start());
    assertEquals(expectedEnd2, second.end());
  }

  static Stream<Arguments> emptyOrNullProvider() {
    return Stream.of(
        Arguments.of(Collections.emptyList()), Arguments.of((List<CircularDayOfWeek>) null));
  }

  static Stream<Arguments> singleRangeProvider() {
    return Stream.of(
        Arguments.of(
            List.of(CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)), 1, "MON", "WED"),
        Arguments.of(
            List.of(CircularDayOfWeek.of(DayOfWeek.FRIDAY, DayOfWeek.MONDAY)), 1, "FRI", "MON"),
        Arguments.of(
            List.of(CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY)), 1, "MON", "MON"));
  }

  static Stream<Arguments> mergeToOneProvider() {
    return Stream.of(
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)),
            "MON",
            "FRI"),
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY),
                CircularDayOfWeek.of(DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)),
            "MON",
            "FRI"),
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.FRIDAY, DayOfWeek.MONDAY),
                CircularDayOfWeek.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY)),
            "FRI",
            "WED"),
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
                CircularDayOfWeek.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY),
                CircularDayOfWeek.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)),
            "MON",
            "SAT"),
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)),
            "MON",
            "SUN"),
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.SATURDAY, DayOfWeek.TUESDAY),
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)),
            "SAT",
            "WED"),
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY),
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY)),
            "MON",
            "MON"),
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.MONDAY),
                CircularDayOfWeek.of(DayOfWeek.TUESDAY, DayOfWeek.TUESDAY)),
            "MON",
            "TUE"),
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY)),
            "MON",
            "FRI"));
  }

  static Stream<Arguments> mergeToTwoProvider() {
    return Stream.of(
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
                CircularDayOfWeek.of(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)),
            "MON",
            "TUE",
            "THU",
            "FRI"),
        Arguments.of(
            List.of(
                CircularDayOfWeek.of(DayOfWeek.FRIDAY, DayOfWeek.SUNDAY),
                CircularDayOfWeek.of(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY)),
            "TUE",
            "WED",
            "FRI",
            "SUN"));
  }
}
