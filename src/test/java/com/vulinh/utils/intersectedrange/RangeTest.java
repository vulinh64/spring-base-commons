package com.vulinh.utils.intersectedrange;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

@SuppressWarnings("java:S5778") // please be silent SonarQube
class RangeTest {

  static final LocalDateTime BASE_TIME = LocalDateTime.of(2024, 1, 1, 0, 0);

  @Test
  void testIsIntersectedNulLRange() {
    assertThrows(
        IntersectedRangeException.class,
        () ->
            Range.<LocalDateTime>builder()
                .from(BASE_TIME)
                .to(BASE_TIME.plusHours(1))
                .build()
                .isIntersected(null));
  }

  @Test
  void testIsIntersectedNonIntersectedRange1() {
    var isIntersected =
        Range.<LocalDateTime>builder()
            .from(BASE_TIME)
            .to(BASE_TIME.plusHours(1))
            .build()
            .isIntersected(
                Range.<LocalDateTime>builder()
                    .from(BASE_TIME.plusHours(2))
                    .to(BASE_TIME.plusHours(3))
                    .build());

    assertFalse(isIntersected);
  }

  @Test
  void testIsIntersectedNonIntersectedRange2() {
    var isIntersected =
        Range.<LocalDateTime>builder()
            .from(BASE_TIME.plusHours(2))
            .to(BASE_TIME.plusHours(3))
            .build()
            .isIntersected(
                Range.<LocalDateTime>builder().from(BASE_TIME).to(BASE_TIME.plusHours(1)).build());

    assertFalse(isIntersected);
  }

  @Test
  void testMergeNonIntersectedRange1() {
    assertThrows(
        IntersectedRangeException.class,
        () ->
            Range.<LocalDateTime>builder()
                .from(BASE_TIME)
                .to(BASE_TIME.plusHours(1))
                .build()
                .merge(
                    Range.<LocalDateTime>builder()
                        .from(BASE_TIME.plusHours(2))
                        .to(BASE_TIME.plusHours(3))
                        .build()));
  }

  @Test
  void testMergeNonIntersectedRange2() {
    assertThrows(
        IntersectedRangeException.class,
        () ->
            Range.<LocalDateTime>builder()
                .from(BASE_TIME.plusHours(2))
                .to(BASE_TIME.plusHours(3))
                .build()
                .merge(
                    Range.<LocalDateTime>builder()
                        .from(BASE_TIME)
                        .to(BASE_TIME.plusHours(1))
                        .build()));
  }
}
