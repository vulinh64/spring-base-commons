package com.vulinh.springcron;

import static com.vulinh.springcron.data.DayExpression.*;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_INT_ARRAY;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

import com.vulinh.exception.SpringCronException;
import com.vulinh.springcron.data.DayExpression;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DayExpressionTest {

  @ParameterizedTest
  @MethodSource("validProvider")
  void testToExpressionValid(DayExpression type, String expected, int... args) {
    assertEquals(expected, type.generateFinalExpression(args));
  }

  static Stream<Arguments> validProvider() {
    return Stream.of(
        of(EVERY_DAY, "*", EMPTY_INT_ARRAY),
        of(EVERY_N_DAY, "*/1", new int[] {1}),
        of(EVERY_N_DAY, "*/5", new int[] {5}),
        of(BETWEEN_DAYS, "1-15", new int[] {15, 1}),
        of(BETWEEN_DAYS, "1-31", new int[] {31, 1}),
        of(SPECIFIC_DAYS, "3,2,1", new int[] {3, 2, 1, 2, 3}),
        of(SPECIFIC_DAYS, "15", new int[] {15}),
        of(SPECIFIC_DAY_RANGES, "1-10,20-25", new int[] {1, 20, 10, 25}),
        of(SPECIFIC_DAY_RANGES, "5-7,15-17", new int[] {5, 15, 7, 17}),
        of(N_TO_LAST_DAY, "L-1", new int[] {1}),
        of(N_TO_LAST_DAY, "L-3", new int[] {3}),
        of(DAY_NO_CARE, "0", new int[] {2, 44, 333}));
  }

  @ParameterizedTest
  @MethodSource("invalidProvider")
  void testToExpressionInvalid(DayExpression type, int... args) {
    assertThrows(SpringCronException.class, () -> type.generateFinalExpression(args));
  }

  static Stream<Arguments> invalidProvider() {
    return Stream.of(
        of(BETWEEN_DAYS, new int[] {10, -1}),
        of(BETWEEN_DAYS, new int[] {32, 10}),
        of(SPECIFIC_DAY_RANGES, new int[] {1, 20, 10, 35}),
        of(SPECIFIC_DAY_RANGES, new int[] {-1, 20, 10, 25}),
        of(SPECIFIC_DAY_RANGES, new int[] {1, 10, 7}),
        of(SPECIFIC_DAY_RANGES, new int[] {1, 10, 1, 6, 1, 5}),
        of(EVERY_N_DAY, new int[] {0}),
        of(EVERY_N_DAY, new int[] {-2}),
        of(SPECIFIC_DAYS, new int[] {0}),
        of(SPECIFIC_DAYS, new int[] {33}),
        of(N_TO_LAST_DAY, new int[] {0}),
        of(N_TO_LAST_DAY, new int[] {40}));
  }
}
