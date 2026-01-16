package com.vulinh.utils.springcron;

import static org.apache.commons.lang3.ArrayUtils.EMPTY_INT_ARRAY;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

import com.vulinh.exception.SpringCronException;
import com.vulinh.utils.springcron.data.MonthExpression;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MonthExpressionTest {

  @ParameterizedTest
  @MethodSource("validProvider")
  void testMonthExpressionValid(MonthExpression type, String expected, int... args) {
    assertEquals(expected, type.generateFinalExpression(args));
  }

  @ParameterizedTest
  @MethodSource("invalidProvider")
  void testMonthExpressionInvalid(MonthExpression type, int... args) {
    assertThrows(SpringCronException.class, () -> type.generateFinalExpression(args));
  }

  public static Stream<Arguments> validProvider() {
    return Stream.of(
        of(MonthExpression.EVERY_MONTH, "*", new int[] {12, 16, 1}),
        of(MonthExpression.EVERY_N_MONTH, "*/2", new int[] {2}),
        of(MonthExpression.BETWEEN_MONTHS, "JAN-JUL", new int[] {1, 7}),
        of(MonthExpression.BETWEEN_MONTHS, "DEC-FEB", new int[] {12, 2}),
        of(MonthExpression.SPECIFIC_MONTHS, "MAR,JAN,FEB", new int[] {3, 1, 2, 3}),
        of(MonthExpression.SPECIFIC_MONTH_INTERVAL, "APR-DEC", new int[] {4, 10, 6, 12}),
        of(MonthExpression.SPECIFIC_MONTH_INTERVAL, "DEC-APR", new int[] {12, 2, 3, 4}));
  }

  public static Stream<Arguments> invalidProvider() {
    return Stream.of(
        of(MonthExpression.EVERY_N_MONTH, new int[] {0}),
        of(MonthExpression.EVERY_N_MONTH, new int[] {13}),
        of(MonthExpression.EVERY_N_MONTH, EMPTY_INT_ARRAY),
        of(MonthExpression.BETWEEN_MONTHS, new int[] {0, 5}),
        of(MonthExpression.BETWEEN_MONTHS, new int[] {5, 13}),
        of(MonthExpression.BETWEEN_MONTHS, new int[] {5, 13}),
        of(MonthExpression.SPECIFIC_MONTHS, new int[] {0, 5, 3}),
        of(MonthExpression.SPECIFIC_MONTHS, new int[] {2, 14}),
        of(MonthExpression.SPECIFIC_MONTH_INTERVAL, new int[] {3, 8, 5, 15}),
        of(MonthExpression.SPECIFIC_MONTH_INTERVAL, EMPTY_INT_ARRAY));
  }
}
