package com.vulinh.utils.intersectedrange;

import static com.vulinh.utils.intersectedrange.Merger.mergeRanges;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class MergerTest {

  static final LocalDateTime BASE_TIME = LocalDateTime.of(2024, 1, 1, 0, 0);

  @Test
  void testNullList() {
    assertTrue(mergeRanges(null).isEmpty());
  }

  @Test
  void testEmptyList() {
    assertTrue(mergeRanges(emptyList()).isEmpty());
  }

  @Test
  void testNoIntersectedRanges() {
    var ranges =
        List.of(Range.<LocalDateTime>builder().from(BASE_TIME).to(BASE_TIME.plusHours(1)).build());

    assertEquals(ranges, mergeRanges(ranges));
  }

  @Test
  void testNoIntersectedRanges2() {
    var ranges =
        List.of(
            Range.<LocalDateTime>builder().from(BASE_TIME).to(BASE_TIME.plusHours(1)).build(),
            Range.<LocalDateTime>builder()
                .from(BASE_TIME.plusHours(2))
                .to(BASE_TIME.plusHours(3))
                .build());

    var result = mergeRanges(ranges);

    assertEquals(2, result.size());
    assertEquals(ranges, result);
  }

  @Test
  void testIntersectedRanges() {
    var result =
        mergeRanges(
            List.of(
                Range.<LocalDateTime>builder().from(BASE_TIME).to(BASE_TIME.plusHours(2)).build(),
                Range.<LocalDateTime>builder()
                    .from(BASE_TIME.plusHours(1))
                    .to(BASE_TIME.plusHours(3))
                    .build()));

    assertAll(
        () -> assertEquals(1, result.size()),
        () -> assertEquals(BASE_TIME, result.get(0).getFrom()),
        () -> assertEquals(BASE_TIME.plusHours(3), result.get(0).getTo()));
  }

  @Test
  void testUnsortedMultipleRanges() {
    var result =
        mergeRanges(
            List.of(
                Range.<LocalDateTime>builder()
                    .from(BASE_TIME.plusHours(6))
                    .to(BASE_TIME.plusHours(8))
                    .build(),
                Range.<LocalDateTime>builder().from(BASE_TIME).to(BASE_TIME.plusHours(2)).build(),
                Range.<LocalDateTime>builder()
                    .from(BASE_TIME.plusHours(12))
                    .to(BASE_TIME.plusHours(14))
                    .build(),
                Range.<LocalDateTime>builder()
                    .from(BASE_TIME.plusHours(1))
                    .to(BASE_TIME.plusHours(4))
                    .build(),
                Range.<LocalDateTime>builder()
                    .from(BASE_TIME.plusHours(7))
                    .to(BASE_TIME.plusHours(10))
                    .build()));

    assertAll(
        () -> assertEquals(3, result.size()),
        () ->
            assertEquals(
                Range.<LocalDateTime>builder().from(BASE_TIME).to(BASE_TIME.plusHours(4)).build(),
                result.get(0)),
        () ->
            assertEquals(
                Range.<LocalDateTime>builder()
                    .from(BASE_TIME.plusHours(6))
                    .to(BASE_TIME.plusHours(10))
                    .build(),
                result.get(1)),
        () ->
            assertEquals(
                Range.<LocalDateTime>builder()
                    .from(BASE_TIME.plusHours(12))
                    .to(BASE_TIME.plusHours(14))
                    .build(),
                result.get(2)));
  }

  @Test
  void testMultipleRanges2() {
    var result =
        mergeRanges(
            List.of(
                Range.<LocalDateTime>builder().from(BASE_TIME).to(BASE_TIME.plusHours(5)).build(),
                Range.<LocalDateTime>builder()
                    .from(BASE_TIME.plusHours(1))
                    .to(BASE_TIME.plusHours(3))
                    .build()));

    assertAll(
        () -> assertEquals(1, result.size()),
        () ->
            assertEquals(
                Range.<LocalDateTime>builder().from(BASE_TIME).to(BASE_TIME.plusHours(5)).build(),
                result.get(0)));
  }

  @Test
  void testMultipleRange3() {
    var result =
        mergeRanges(
            List.of(
                Range.<LocalDateTime>builder().from(BASE_TIME).to(BASE_TIME.plusHours(2)).build(),
                Range.<LocalDateTime>builder()
                    .from(BASE_TIME.plusHours(2))
                    .to(BASE_TIME.plusHours(4))
                    .build()));

    assertAll(
        () -> assertEquals(1, result.size()),
        () ->
            assertEquals(
                Range.<LocalDateTime>builder().from(BASE_TIME).to(BASE_TIME.plusHours(4)).build(),
                result.get(0)));
  }
}
