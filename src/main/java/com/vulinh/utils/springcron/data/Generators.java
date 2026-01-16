package com.vulinh.utils.springcron.data;

import com.vulinh.utils.CommonUtils;
import com.vulinh.utils.circularrange.*;
import com.vulinh.utils.intersectedrange.IntersectedRangeMerger;
import com.vulinh.utils.intersectedrange.Range;
import com.vulinh.utils.springcron.Interval;
import com.vulinh.utils.springcron.IntervalType;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/** Utility class providing common functions for generating cron expressions. */
class Generators {

  static final String COMMA = ",";

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
        .collect(Collectors.joining(COMMA));
  }

  /**
   * Generates a cron expression for an interval between two values based on the provided list,
   * toTextTransformer, and interval type.
   *
   * @param list List containing two integer values defining the interval.
   * @param toTextTransformer Function to transform integer values to strings.
   * @return A string representing the interval in the format "e1-e2".
   */
  static String betweenExpression(List<Integer> list, IntFunction<String> toTextTransformer) {
    return createSingleInterval(
        toTextTransformer, Interval.of(list.get(0), list.get(1), IntervalType.INFLEXIBLE));
  }

  /**
   * Generates a cron expression for multiple mergeable intervals based on the provided list.
   *
   * @param list List of integer values representing multiple intervals.
   * @return A comma-separated string of merged intervals in the format "e1-e2,e3-e4,...".
   */
  static String inflexibleMergeableIntervalExpression(List<Integer> list) {
    var ranges = new LinkedList<Range<Integer>>();

    for (var i = 0; i < list.size() - 1; i += 2) {
      ranges.add(Range.<Integer>builder().from(list.get(i)).to(list.get(i + 1)).build());
    }

    return IntersectedRangeMerger.mergeRanges(ranges).stream()
        .map(range -> CommonUtils.FROM_TO.formatted(range.getFrom(), range.getTo()))
        .collect(Collectors.joining(COMMA));
  }

  /**
   * Creates a string representation of a single interval using the provided {@code
   * toTextTransformer}.
   *
   * @param toTextTransformer Function to transform integer values to strings.
   * @param interval The interval object containing start and end values.
   * @return String representation of the interval in the format "e1-e2".
   */
  static String createSingleInterval(IntFunction<String> toTextTransformer, Interval interval) {
    return CommonUtils.FROM_TO.formatted(
        toTextTransformer.apply(interval.start()), toTextTransformer.apply(interval.end()));
  }

  /**
   * Generate expression for circular month ranges.
   *
   * @param rawList list of integers representing month ranges
   * @return string representation of circular month ranges
   */
  static String monthCircularRanges(List<Integer> rawList) {
    return toMultipleCircularRanges(rawList, Month::of, CircularMonth::of);
  }

  /**
   * Generate expression for circular day of week ranges.
   *
   * @param rawList list of integers representing day of week ranges
   * @return string representation of circular day of week ranges
   */
  static String dayOfWeekCircularRanges(List<Integer> rawList) {
    return toMultipleCircularRanges(
        rawList, day -> day == 0 ? DayOfWeek.SUNDAY : DayOfWeek.of(day), CircularDayOfWeek::of);
  }

  static <T extends Comparable<? super T>> String toMultipleCircularRanges(
      List<Integer> rawList,
      IntFunction<? extends T> fromIntToValueMapper,
      BiFunction<? super T, ? super T, ? extends CircularRange<T>> toCircularRangeMapper) {
    var segments = new LinkedList<CircularRange<T>>();

    for (var i = 0; i < rawList.size(); i += 2) {
      segments.add(
          toCircularRangeMapper.apply(
              fromIntToValueMapper.apply(rawList.get(i)),
              fromIntToValueMapper.apply(rawList.get(i + 1))));
    }

    return CircularRangeMerger.mergeCircularRanges(segments).stream()
        .map(TransformedSegment::toRangeRepresent)
        .collect(Collectors.joining(","));
  }
}
