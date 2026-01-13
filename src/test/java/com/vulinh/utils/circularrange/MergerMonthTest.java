package com.vulinh.utils.circularrange;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MergerMonthTest {

  @ParameterizedTest
  @MethodSource("emptyOrNullProvider")
  void testMergeCircularRangesEmptyOrNull(List<CircularMonth> input) {
    assertTrue(Merger.mergeCircularRanges(input).isEmpty());
  }

  @ParameterizedTest
  @MethodSource("singleRangeProvider")
  void testMergeCircularRangesSingleRange(
      List<CircularMonth> input, int expectedSize, String expectedStart, String expectedEnd) {
    var result = Merger.mergeCircularRanges(input);

    assertEquals(expectedSize, result.size());

    var first = result.get(0);

    assertEquals(expectedStart, first.start());
    assertEquals(expectedEnd, first.end());
  }

  @ParameterizedTest
  @MethodSource("mergeToOneProvider")
  void testMergeCircularRangesMergeToOne(
      List<CircularMonth> input, String expectedStart, String expectedEnd) {
    var result = Merger.mergeCircularRanges(input);

    assertEquals(1, result.size());

    var first = result.get(0);

    assertEquals(expectedStart, first.start());
    assertEquals(expectedEnd, first.end());
  }

  @ParameterizedTest
  @MethodSource("mergeToTwoProvider")
  void testMergeCircularRangesMergeToTwo(
      List<CircularMonth> input,
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
        Arguments.of(Collections.emptyList()), Arguments.of((List<CircularMonth>) null));
  }

  static Stream<Arguments> singleRangeProvider() {
    return Stream.of(
        Arguments.of(List.of(CircularMonth.of(Month.JANUARY, Month.MARCH)), 1, "JAN", "MAR"),
        Arguments.of(List.of(CircularMonth.of(Month.DECEMBER, Month.FEBRUARY)), 1, "DEC", "FEB"),
        Arguments.of(List.of(CircularMonth.of(Month.JULY, Month.JULY)), 1, "JUL", "JUL"));
  }

  static Stream<Arguments> mergeToOneProvider() {
    return Stream.of(
        Arguments.of(
            List.of(
                CircularMonth.of(Month.JANUARY, Month.MARCH),
                CircularMonth.of(Month.APRIL, Month.JUNE)),
            "JAN",
            "JUN"),
        Arguments.of(
            List.of(
                CircularMonth.of(Month.JANUARY, Month.APRIL),
                CircularMonth.of(Month.MARCH, Month.JUNE)),
            "JAN",
            "JUN"),
        Arguments.of(
            List.of(
                CircularMonth.of(Month.DECEMBER, Month.FEBRUARY),
                CircularMonth.of(Month.MARCH, Month.MAY)),
            "DEC",
            "MAY"),
        Arguments.of(
            List.of(
                CircularMonth.of(Month.JANUARY, Month.FEBRUARY),
                CircularMonth.of(Month.MARCH, Month.APRIL),
                CircularMonth.of(Month.MAY, Month.JUNE)),
            "JAN",
            "JUN"),
        Arguments.of(
            List.of(
                CircularMonth.of(Month.JUNE, Month.JULY),
                CircularMonth.of(Month.AUGUST, Month.DECEMBER)),
            "JUN",
            "DEC"),
        Arguments.of(
            List.of(
                CircularMonth.of(Month.JUNE, Month.SEPTEMBER),
                CircularMonth.of(Month.AUGUST, Month.NOVEMBER)),
            "JUN",
            "NOV"),
        Arguments.of(
            List.of(
                CircularMonth.of(Month.JANUARY, Month.JANUARY),
                CircularMonth.of(Month.JANUARY, Month.JANUARY)),
            "JAN",
            "JAN"),
        Arguments.of(
            List.of(
                CircularMonth.of(Month.JANUARY, Month.JANUARY),
                CircularMonth.of(Month.FEBRUARY, Month.FEBRUARY)),
            "JAN",
            "FEB"),
        Arguments.of(
            List.of(
                CircularMonth.of(Month.OCTOBER, Month.DECEMBER),
                CircularMonth.of(Month.JANUARY, Month.SEPTEMBER)),
            "JAN",
            "DEC"));
  }

  static Stream<Arguments> mergeToTwoProvider() {
    return Stream.of(
        Arguments.of(
            List.of(
                CircularMonth.of(Month.JANUARY, Month.FEBRUARY),
                CircularMonth.of(Month.APRIL, Month.MAY)),
            "JAN",
            "FEB",
            "APR",
            "MAY"),
        Arguments.of(
            List.of(
                CircularMonth.of(Month.NOVEMBER, Month.DECEMBER),
                CircularMonth.of(Month.FEBRUARY, Month.MARCH)),
            "FEB",
            "MAR",
            "NOV",
            "DEC"));
  }
}
