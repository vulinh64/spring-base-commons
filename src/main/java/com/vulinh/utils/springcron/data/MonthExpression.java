package com.vulinh.utils.springcron.data;

import com.vulinh.utils.springcron.Interval;
import com.vulinh.utils.springcron.IntervalType;
import com.vulinh.utils.springcron.PartExpression;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** Enumeration representing different expressions for months in a cron expression. */
public enum MonthExpression implements PartExpression {

  /** Expression representing every month. */
  EVERY_MONTH(Validators.alwaysTrue(), Generators.every()),

  /** Expression representing every N months. */
  EVERY_N_MONTH(
      list ->
          Validators.isValidSingletonListWithinBounds(
              list, Constants.MONTH_MIN, Constants.MONTH_MAX),
      Generators::everyNthExpression),

  /** Expression representing an interval between two months. */
  BETWEEN_MONTHS(
      list ->
          Validators.isValidDualListWithinBounds(list, Constants.MONTH_MIN, Constants.MONTH_MAX),
      list -> Generators.betweenExpression(list, IntervalType.FLEXIBLE, Constants.MONTH_MAP::get)),

  /** Expression representing specific values for months. */
  SPECIFIC_MONTHS(
      list -> Validators.isValidListWithinBound(list, Constants.MONTH_MIN, Constants.MONTH_MAX),
      list -> Generators.specificValueExpression(list, Constants.MONTH_MAP::get)),

  /** Expression representing specific intervals for months. */
  SPECIFIC_MONTH_INTERVAL(
      SpecificIntervalValidator.MONTH_INTERVAL_VALIDATOR::isValidMultiIntervalList,
      list -> {
        var sortedList = list.stream().distinct().sorted().toList();

        var result = new LinkedList<Interval>();

        for (int i = 0; i < sortedList.size(); i = i + 2) {
          var first = sortedList.get(i);
          var second = sortedList.get(i + 1);

          result.add(Interval.of(first, second));
        }

        return result.stream()
            .map(interval -> Generators.createSingleInterval(Constants.MONTH_MAP::get, interval))
            .collect(Collectors.joining(Generators.COMMA));
      }),

  /** Expression representing no specific care for months. */
  MONTH_NO_CARE(Validators.alwaysTrue(), Generators.noCare());

  final Predicate<List<Integer>> predicate;
  final Function<List<Integer>, String> generator;

  MonthExpression(Predicate<List<Integer>> predicate, Function<List<Integer>, String> generator) {
    this.predicate = predicate;
    this.generator = generator;
  }

  @Override
  public Predicate<List<Integer>> getPredicate() {
    return predicate;
  }

  @Override
  public Function<List<Integer>, String> getGenerator() {
    return generator;
  }
}
