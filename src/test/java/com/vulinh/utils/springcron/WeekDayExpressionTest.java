package com.vulinh.utils.springcron;

import static com.vulinh.utils.springcron.data.WeekDayExpression.*;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_INT_ARRAY;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

import com.vulinh.exception.SpringCronException;
import com.vulinh.utils.springcron.data.WeekDayExpression;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class WeekDayExpressionTest {

  @ParameterizedTest
  @MethodSource("validProvider")
  void testDayOfWeekExpressionValid(WeekDayExpression expr, String expected, int... args) {
    assertEquals(expected, expr.generateFinalExpression(args));
  }

  // Additional invalid cases for robustness
  @ParameterizedTest
  @MethodSource("invalidProvider")
  void testDayOfWeekExpressionInvalid(WeekDayExpression expr, int... args) {
    assertThrows(SpringCronException.class, () -> expr.generateFinalExpression(args));
  }

  static Stream<Arguments> validProvider() {
    return Stream.of(
        of(EVERY_WEEK_DAY, "*", new int[] {1, 2, 3}),
        of(EVERY_N_WEEK_DAY, "*/3", new int[] {3}),
        of(BETWEEN_WEEK_DAYS, "MON-FRI", new int[] {1, 5}),
        of(BETWEEN_WEEK_DAYS, "FRI-SUN", new int[] {5, 0}),
        of(BETWEEN_WEEK_DAYS, "FRI-SUN", new int[] {5, 7}),
        of(BETWEEN_WEEK_DAYS, "SUN-TUE", new int[] {7, 2}),
        of(BETWEEN_WEEK_DAYS, "*", new int[] {0, 6}),
        of(BETWEEN_WEEK_DAYS, "*", new int[] {1, 7}),
        of(SPECIFIC_WEEK_DAYS, "MON,WED,FRI,SUN", new int[] {1, 3, 5, 7, 0}),
        of(NTH_OCCURRENCE, "TUE#2", new int[] {2, 2}),
        of(NTH_OCCURRENCE, "SUN#3", new int[] {0, 3}),
        of(NTH_OCCURRENCE, "SUN#3", new int[] {7, 3}),
        of(LAST_OF_MONTH, "FRIL", new int[] {5}),
        of(LAST_OF_MONTH, "SUNL", new int[] {0}),
        of(LAST_OF_MONTH, "SUNL", new int[] {7}),
        of(SPECIFIC_WEEK_DAY_INTERVALS, "MON-TUE,THU-FRI", new int[] {1, 2, 4, 5}),
        of(SPECIFIC_WEEK_DAY_INTERVALS, "*", new int[] {0, 2, 3, 6}),
        of(SPECIFIC_WEEK_DAY_INTERVALS, "*", new int[] {5, 7, 1, 4}));
  }

  static Stream<Arguments> invalidProvider() {
    return Stream.of(
        of(EVERY_N_WEEK_DAY, EMPTY_INT_ARRAY),
        of(EVERY_N_WEEK_DAY, new int[] {8}),
        of(EVERY_N_WEEK_DAY, new int[] {-1}),
        of(BETWEEN_WEEK_DAYS, EMPTY_INT_ARRAY),
        of(BETWEEN_WEEK_DAYS, new int[] {1}),
        of(BETWEEN_WEEK_DAYS, new int[] {0, 8}),
        of(BETWEEN_WEEK_DAYS, new int[] {-1, 5}),
        of(BETWEEN_WEEK_DAYS, new int[] {9, 10}),
        of(SPECIFIC_WEEK_DAYS, EMPTY_INT_ARRAY),
        of(SPECIFIC_WEEK_DAYS, new int[] {0, 1, 8}),
        of(SPECIFIC_WEEK_DAYS, new int[] {-1, 2}),
        of(NTH_OCCURRENCE, EMPTY_INT_ARRAY),
        of(NTH_OCCURRENCE, new int[] {2}),
        of(NTH_OCCURRENCE, new int[] {2, 0}),
        of(NTH_OCCURRENCE, new int[] {2, 6}),
        of(NTH_OCCURRENCE, new int[] {8, 2}),
        of(NTH_OCCURRENCE, new int[] {-1, 3}),
        of(LAST_OF_MONTH, EMPTY_INT_ARRAY),
        of(LAST_OF_MONTH, new int[] {8}),
        of(LAST_OF_MONTH, new int[] {-1}));
  }
}
