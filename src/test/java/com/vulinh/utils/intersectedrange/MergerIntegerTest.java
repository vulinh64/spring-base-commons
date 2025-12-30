package com.vulinh.utils.intersectedrange;

import static com.vulinh.utils.intersectedrange.Merger.mergeRanges;
import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MergerIntegerTest {

  @Test
  void testNullList() {
    assertTrue(mergeRanges(null).isEmpty());
  }

  @Test
  void testEmptyList() {
    assertTrue(mergeRanges(emptyList()).isEmpty());
  }

  @Test
  void testSingleRange() {
    var ranges = of(Range.<Integer>builder().from(1).to(5).build());

    assertEquals(ranges, mergeRanges(ranges));
  }

  @Test
  void testTwoIntersectedRanges() {
    var result =
        mergeRanges(
            of(
                Range.<Integer>builder().from(1).to(5).build(),
                Range.<Integer>builder().from(3).to(7).build()));

    assertEquals(1, result.size());

    var first = result.get(0);

    assertAll(() -> assertEquals(1, first.from()), () -> assertEquals(7, first.to()));
  }

  @Test
  void testTwoNonIntersectedRanges() {
    var result =
        mergeRanges(
            of(
                Range.<Integer>builder().from(1).to(2).build(),
                Range.<Integer>builder().from(3).to(4).build()));

    assertEquals(2, result.size());
    assertEquals(
        of(
            Range.<Integer>builder().from(1).to(2).build(),
            Range.<Integer>builder().from(3).to(4).build()),
        result);
  }

  @Test
  void testMultipleRangesWithOverlaps() {
    var result =
        mergeRanges(
            of(
                Range.<Integer>builder().from(6).to(8).build(),
                Range.<Integer>builder().from(1).to(3).build(),
                Range.<Integer>builder().from(12).to(14).build(),
                Range.<Integer>builder().from(2).to(4).build(),
                Range.<Integer>builder().from(7).to(10).build()));

    assertAll(
        () -> assertEquals(3, result.size()),
        () -> assertEquals(Range.<Integer>builder().from(1).to(4).build(), result.get(0)),
        () -> assertEquals(Range.<Integer>builder().from(6).to(10).build(), result.get(1)),
        () -> assertEquals(Range.<Integer>builder().from(12).to(14).build(), result.get(2)));
  }

  @Test
  void testContainedRange() {
    var result =
        mergeRanges(
            of(
                Range.<Integer>builder().from(1).to(10).build(),
                Range.<Integer>builder().from(3).to(7).build()));

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals(Range.<Integer>builder().from(1).to(10).build(), result.get(0)));
  }

  @Test
  void testAdjacentRanges() {
    var result =
        mergeRanges(
            of(
                Range.<Integer>builder().from(1).to(5).build(),
                Range.<Integer>builder().from(5).to(10).build()));

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals(Range.<Integer>builder().from(1).to(10).build(), result.get(0)));
  }
}
