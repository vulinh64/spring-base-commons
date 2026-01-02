package com.vulinh.utils.springcron.data;

import com.vulinh.utils.springcron.PartExpression;
import com.vulinh.utils.springcron.RangeType;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/** Enumeration representing different expressions for hours in a cron expression. */
public enum HourExpression implements PartExpression {

  /** Expression representing every hour. */
  EVERY_HOUR(Validators.alwaysTrue(), Generators.every()),

  /** Expression representing every N hours. */
  EVERY_N_HOUR(
      list ->
          Validators.isValidSingletonListWithinBounds(list, Constants.HOUR_ONE, Constants.HOUR_MAX),
      Generators::everyNthExpression),

  /** Expression representing a range between two hours. */
  BETWEEN_HOURS(
      list -> Validators.isValidDualListWithinBounds(list, Constants.HOUR_MIN, Constants.HOUR_MAX),
      list -> Generators.betweenExpression(list, RangeType.INFLEXIBLE, String::valueOf)),

  /** Expression representing specific values for hours. */
  SPECIFIC_HOURS(
      list -> Validators.isValidListWithinRange(list, Constants.HOUR_MIN, Constants.HOUR_MAX),
      list -> Generators.specificValueExpression(list, String::valueOf)),

  /** Expression representing specific ranges for hours. */
  SPECIFIC_HOUR_RANGES(
      SpecificIntervalValidator.HOUR_INTERVAL_VALIDATOR::isValidMultiIntervalList,
      list -> Generators.rangeExpression(list, String::valueOf, RangeType.INFLEXIBLE)),

  /** Expression representing no specific care for hours. */
  HOUR_NO_CARE(Validators.alwaysTrue(), Generators.noCare());

  final Predicate<List<Integer>> predicate;
  final Function<List<Integer>, String> generator;

  HourExpression(Predicate<List<Integer>> predicate, Function<List<Integer>, String> generator) {
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
