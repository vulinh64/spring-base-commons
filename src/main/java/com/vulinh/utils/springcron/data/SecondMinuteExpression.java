package com.vulinh.utils.springcron.data;

import com.vulinh.utils.springcron.PartExpression;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/** Enumeration representing different expressions for seconds and minutes in a cron expression. */
public enum SecondMinuteExpression implements PartExpression {

  /** Expression representing every second or minute. */
  EVERY(Validators.alwaysTrue(), Generators.every()),

  /** Expression representing every N seconds or minutes. */
  EVERY_N(
      list ->
          Validators.isValidSingletonListWithinBounds(
              list, Constants.SECOND_MINUTE_ONE, Constants.SECOND_MINUTE_MAX),
      Generators::everyNthExpression),

  /** Expression representing an interval between two seconds or minutes. */
  BETWEEN(
      list ->
          Validators.isValidDualListWithinBounds(list, Constants.ZERO, Constants.SECOND_MINUTE_MAX),
      list -> Generators.betweenExpression(list, String::valueOf)),

  /** Expression representing specific values for seconds or minutes. */
  SPECIFIC_VALUES(
      list -> Validators.isValidListWithinBound(list, Constants.ZERO, Constants.SECOND_MINUTE_MAX),
      list -> Generators.specificValueExpression(list, String::valueOf)),

  /** Expression representing specific intervals for seconds or minutes. */
  SPECIFIC_INTERVALS(
      SpecificIntervalValidator.MINUTE_SECOND_INTERVAL_VALIDATOR::isValidMultiIntervalList,
      Generators::inflexibleMergeableIntervalExpression),

  /** Expression representing no specific care for seconds or minutes (i.e., wildcard). */
  NO_CARE(Validators.alwaysTrue(), Generators.noCare());

  final Predicate<List<Integer>> predicate;
  final Function<List<Integer>, String> generator;

  SecondMinuteExpression(
      Predicate<List<Integer>> predicate, Function<List<Integer>, String> generator) {
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
