package com.vulinh.utils.springcron;

import static com.vulinh.utils.springcron.data.HourExpression.*;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_INT_ARRAY;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

import com.vulinh.exception.SpringCronException;
import com.vulinh.utils.springcron.data.HourExpression;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HourExpressionTest {

  @ParameterizedTest
  @MethodSource("validProvider")
  void testHourExpressionValid(HourExpression type, String expected, int... args) {
    assertEquals(expected, type.generateFinalExpression(args));
  }

  @ParameterizedTest
  @MethodSource("invalidProvider")
  void testHourExpressionInvalid(HourExpression type, int... args) {
    assertThrows(SpringCronException.class, () -> type.generateFinalExpression(args));
  }

  static Stream<Arguments> validProvider() {
    return Stream.of(
        of(EVERY_HOUR, "*", EMPTY_INT_ARRAY),
        of(EVERY_N_HOUR, "*/1", new int[] {1}),
        of(EVERY_N_HOUR, "*/3", new int[] {3}),
        of(BETWEEN_HOURS, "1-5", new int[] {5, 1, 29}),
        of(BETWEEN_HOURS, "0-23", new int[] {23, 0}),
        of(SPECIFIC_HOURS, "0", new int[] {0}),
        of(SPECIFIC_HOURS, "3,2,1", new int[] {3, 2, 1, 2, 3}),
        of(SPECIFIC_HOUR_RANGES, "1-5,10-15", new int[] {1, 10, 5, 15}),
        of(SPECIFIC_HOUR_RANGES, "0-3,20-23", new int[] {0, 20, 3, 23}));
  }

  static Stream<Arguments> invalidProvider() {
    return Stream.of(
        of(BETWEEN_HOURS, new int[] {10, -1}),
        of(BETWEEN_HOURS, new int[] {24, 10}),
        of(BETWEEN_HOURS, new int[] {10}),
        of(SPECIFIC_HOUR_RANGES, new int[] {0, 10, 5, 25}),
        of(SPECIFIC_HOUR_RANGES, EMPTY_INT_ARRAY),
        of(EVERY_N_HOUR, new int[] {0}),
        of(EVERY_N_HOUR, new int[] {56}),
        of(SPECIFIC_HOURS, new int[] {-1}),
        of(SPECIFIC_HOURS, new int[] {30}));
  }
}
