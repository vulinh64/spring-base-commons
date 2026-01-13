package com.vulinh.utils.circularrange;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MergerIntTest {

  @ParameterizedTest
  @MethodSource("emptyOrNullProvider")
  void testMergeCircularRangesEmptyOrNull(List<IntCircularRangeImpl> input) {
    assertTrue(Merger.mergeCircularRanges(input).isEmpty());
  }

  @ParameterizedTest
  @MethodSource("singleRangeProvider")
  void testMergeCircularRangesSingleRange(
      List<IntCircularRangeImpl> input, int expectedSize, String expectedStart, String expectedEnd) {
    var result = Merger.mergeCircularRanges(input);
    assertEquals(expectedSize, result.size());
    var first = result.get(0);
    assertEquals(expectedStart, first.start());
    assertEquals(expectedEnd, first.end());
  }

  @ParameterizedTest
  @MethodSource("mergeToOneProvider")
  void testMergeCircularRangesMergeToOne(
      List<IntCircularRangeImpl> input, String expectedStart, String expectedEnd) {
    var result = Merger.mergeCircularRanges(input);
    assertEquals(1, result.size());
    var first = result.get(0);
    assertEquals(expectedStart, first.start());
    assertEquals(expectedEnd, first.end());
  }

  @ParameterizedTest
  @MethodSource("mergeToTwoProvider")
  void testMergeCircularRangesMergeToTwo(
      List<IntCircularRangeImpl> input,
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
        Arguments.of(Collections.emptyList()), Arguments.of((List<IntCircularRangeImpl>) null));
  }

  static Stream<Arguments> singleRangeProvider() {
    return Stream.of(
        Arguments.of(List.of(IntCircularRangeImpl.of(0, 5)), 1, "0", "5"),
        Arguments.of(List.of(IntCircularRangeImpl.of(22, 2)), 1, "22", "2"),
        Arguments.of(List.of(IntCircularRangeImpl.of(7, 7)), 1, "7", "7"));
  }

  static Stream<Arguments> mergeToOneProvider() {
    return Stream.of(
        Arguments.of(
            List.of(IntCircularRangeImpl.of(0, 5), IntCircularRangeImpl.of(6, 10)),
            "0", "10"),
        Arguments.of(
            List.of(IntCircularRangeImpl.of(0, 8), IntCircularRangeImpl.of(7, 15)),
            "0", "15"),
        Arguments.of(
            List.of(IntCircularRangeImpl.of(22, 2), IntCircularRangeImpl.of(3, 6)),
            "22", "6"),
        Arguments.of(
            List.of(IntCircularRangeImpl.of(0, 3), IntCircularRangeImpl.of(4, 8), IntCircularRangeImpl.of(9, 12)),
            "0", "12"),
        Arguments.of(
            List.of(IntCircularRangeImpl.of(12, 15), IntCircularRangeImpl.of(16, 23)),
            "12", "23"),
        Arguments.of(
            List.of(IntCircularRangeImpl.of(12, 18), IntCircularRangeImpl.of(17, 21)),
            "12", "21"),
        Arguments.of(
            List.of(IntCircularRangeImpl.of(0, 0), IntCircularRangeImpl.of(0, 0)),
            "0", "0"),
        Arguments.of(
            List.of(IntCircularRangeImpl.of(0, 0), IntCircularRangeImpl.of(1, 1)),
            "0", "1"),
        Arguments.of(
            List.of(IntCircularRangeImpl.of(20, 23), IntCircularRangeImpl.of(0, 19)),
            "0", "23"));
  }

  static Stream<Arguments> mergeToTwoProvider() {
    return Stream.of(
        Arguments.of(
            List.of(IntCircularRangeImpl.of(0, 3), IntCircularRangeImpl.of(5, 8)),
            "0", "3", "5", "8"),
        Arguments.of(
            List.of(IntCircularRangeImpl.of(18, 23), IntCircularRangeImpl.of(2, 7)),
            "2", "7", "18", "23"));
  }
}

