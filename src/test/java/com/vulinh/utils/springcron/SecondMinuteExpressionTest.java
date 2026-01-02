package com.vulinh.utils.springcron;

import static com.vulinh.utils.springcron.data.SecondMinuteExpression.*;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_INT_ARRAY;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

import com.vulinh.exception.SpringCronException;
import com.vulinh.utils.springcron.data.SecondMinuteExpression;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SecondMinuteExpressionTest {

  @ParameterizedTest
  @MethodSource("expressionProvider")
  void testToExpression(SecondMinuteExpression type, String expected, int... args) {
    assertEquals(expected, type.generateFinalExpression(args));
  }

  static Stream<Arguments> expressionProvider() {
    return Stream.of(
        of(EVERY, "*", EMPTY_INT_ARRAY),
        of(EVERY_N, "*/15", new int[] {15}),
        of(SPECIFIC_VALUES, "5,3,1", new int[] {5, 3, 3, 1}),
        of(SPECIFIC_VALUES, "0", new int[] {0}),
        of(SPECIFIC_VALUES, "3,2,1", new int[] {3, 2, 1, 2, 3, 1}),
        of(BETWEEN, "10-20", new int[] {20, 10}),
        of(BETWEEN, "0-59", new int[] {59, 0}),
        of(SPECIFIC_RANGES, "5-10,15-20", new int[] {5, 15, 10, 20}),
        of(SPECIFIC_RANGES, "0-10,30-40", new int[] {0, 30, 10, 40}),
        of(NO_CARE, "0", new int[] {9, 46, 7, 144}));
  }

  @ParameterizedTest
  @MethodSource("invalidExpressionProvider")
  void testToExpressionInvalid(SecondMinuteExpression expr, int... args) {
    assertThrows(SpringCronException.class, () -> expr.generateFinalExpression(args));
  }

  static Stream<Arguments> invalidExpressionProvider() {
    return Stream.of(
        of(BETWEEN, new int[] {10, -1}),
        of(BETWEEN, new int[] {60, 10}),
        of(SPECIFIC_RANGES, new int[] {0, 30, 10, 70}),
        of(SPECIFIC_RANGES, new int[] {0, 30, 10, 40, 20}),
        of(SPECIFIC_VALUES, EMPTY_INT_ARRAY));
  }
}
