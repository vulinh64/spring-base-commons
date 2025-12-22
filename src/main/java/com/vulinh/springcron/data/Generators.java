package com.vulinh.springcron.data;

import com.vulinh.springcron.Range;
import com.vulinh.springcron.RangeType;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/** Utility class providing common functions for generating cron expressions. */
class Generators {

  private Generators() {}

  /**
   * Generates a cron expression representing "every" occurrence.
   *
   * @return A function that takes any input and returns the string "*".
   * @param <T> The type of the input parameter (not used).
   */
  static <T> Function<T, String> every() {
    return ignored -> "*";
  }

  /**
   * Generates a cron expression representing "no specific value" or "don't care".
   *
   * @return A function that takes a list of integers and returns the string "0".
   * @param <T> The type of the input parameter (not used).
   */
  static <T> Function<List<T>, String> noCare() {
    return ignored -> "0";
  }

  /**
   * Get first three letters of an enum value.
   *
   * @param value enum value
   * @return first three letters of the enum value.
   */
  static String first3Letters(Enum<?> value) {
    return value.name().substring(0, 3);
  }

  /**
   * Generates a cron expression for every Nth occurrence based on the provided list.
   *
   * @param list List containing a single integer representing the Nth value.
   * @return A string in the format *&#47N, where N is the first element of the list.
   */
  static String everyNthExpression(List<Integer> list) {
    return "*/%s".formatted(list.get(0));
  }

  /**
   * Generates a cron expression for specific values based on the provided list and
   * toTextTransformer.
   *
   * @param list List of integer values to be transformed.
   * @param toTextTransformer Function to transform integer values to strings.
   * @return A comma-separated string of distinct transformed values.
   */
  static String specificValueExpression(List<Integer> list, IntFunction<String> toTextTransformer) {
    return list.stream()
        .mapToInt(Integer::intValue)
        .mapToObj(toTextTransformer)
        .distinct()
        .collect(Collectors.joining(","));
  }

  /**
   * Generates a cron expression for a range between two values based on the provided list,
   * toTextTransformer, and range type.
   *
   * @param list List containing two integer values defining the range.
   * @param toTextTransformer Function to transform integer values to strings.
   * @return A string representing the range in the format "e1-e2".
   */
  static String betweenExpression(
      List<Integer> list, RangeType rangeType, IntFunction<String> toTextTransformer) {
    return createSingleRange(toTextTransformer, Range.of(list.get(0), list.get(1), rangeType));
  }

  /**
   * Generates a cron expression for multiple ranges based on the provided list,
   *
   * @param list List of integer values defining multiple ranges.
   * @param toTextTransformer Function to transform integer values to strings.
   * @param rangeType Type of range (flexible or inflexible).
   * @return A comma-separated string representing multiple ranges.
   */
  static String rangeExpression(
      List<Integer> list, IntFunction<String> toTextTransformer, RangeType rangeType) {
    var sortedList = list.stream().distinct().sorted().toList();

    var result = new LinkedList<Range>();

    for (int i = 0; i < sortedList.size(); i = i + 2) {
      var first = sortedList.get(i);
      var second = sortedList.get(i + 1);

      result.add(
          rangeType == RangeType.FLEXIBLE
              ? new Range(first, second)
              : new Range(Math.min(first, second), Math.max(first, second)));
    }

    return result.stream()
        .map(s -> createSingleRange(toTextTransformer, s))
        .collect(Collectors.joining(","));
  }

  /**
   * Creates a string representation of a single range using the provided {@code toTextTransformer}.
   *
   * @param toTextTransformer Function to transform integer values to strings.
   * @param range The range object containing start and end values.
   * @return String representation of the range in the format "e1-e2".
   */
  private static String createSingleRange(IntFunction<String> toTextTransformer, Range range) {
    return "%s-%s"
        .formatted(toTextTransformer.apply(range.start()), toTextTransformer.apply(range.end()));
  }
}
