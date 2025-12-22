package com.vulinh.springcron.data;

import com.vulinh.springcron.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/** Enumeration representing different expressions for days of the month in a cron expression. */
public enum DayExpression implements PartExpression {

  /** Expression representing every day of the month. */
  EVERY_DAY(Validators.alwaysTrue(), Generators.every()),

  /** Expression representing every N days of the month. */
  EVERY_N_DAY(
      list ->
          Validators.isValidSingletonListWithinBounds(
              list, Constants.DAY_OF_MONTH_MIN, Constants.DAY_OF_MONTH_MAX),
      Generators::everyNthExpression),

  /** Expression representing a range between two days of the month. */
  BETWEEN_DAYS(
      list ->
          Validators.isValidDualListWithinBounds(
              list, Constants.DAY_OF_MONTH_MIN, Constants.DAY_OF_MONTH_MAX),
      list -> Generators.betweenExpression(list, RangeType.INFLEXIBLE, String::valueOf)),

  /** Expression representing specific values for days of the month. */
  SPECIFIC_DAYS(
      list ->
          Validators.isValidListWithinRange(
              list, Constants.DAY_OF_MONTH_MIN, Constants.DAY_OF_MONTH_MAX),
      list -> Generators.specificValueExpression(list, String::valueOf)),

  /** Expression representing specific ranges for days of the month. */
  SPECIFIC_DAY_RANGES(
      SpecificRangeValidator.DAY_OF_MONTH_RANGE_VALIDATOR::isValidMultiRangesList,
      list -> Generators.rangeExpression(list, String::valueOf, RangeType.INFLEXIBLE)),

  /** Expression representing the N day(s) to the end of the month. */
  N_TO_LAST_DAY(
      list ->
          Validators.isValidSingletonListWithinBounds(
              list, Constants.DAY_OF_MONTH_MIN, Constants.DAY_OF_MONTH_MAX),
      list -> String.format("L-%d", list.get(0))),

  /** Expression representing no specific care for day of months. */
  DAY_NO_CARE(Validators.alwaysTrue(), Generators.noCare());

  final Predicate<List<Integer>> predicate;
  final Function<List<Integer>, String> generator;

  DayExpression(Predicate<List<Integer>> predicate, Function<List<Integer>, String> generator) {
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
