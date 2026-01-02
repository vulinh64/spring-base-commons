package com.vulinh.utils.springcron.data;

import com.vulinh.utils.springcron.PartExpression;
import com.vulinh.utils.springcron.RangeType;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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

  /** Expression representing a range between two months. */
  BETWEEN_MONTHS(
      list ->
          Validators.isValidDualListWithinBounds(list, Constants.MONTH_MIN, Constants.MONTH_MAX),
      list -> Generators.betweenExpression(list, RangeType.FLEXIBLE, Constants.MONTH_MAP::get)),

  /** Expression representing specific values for months. */
  SPECIFIC_MONTHS(
      list -> Validators.isValidListWithinRange(list, Constants.MONTH_MIN, Constants.MONTH_MAX),
      list -> Generators.specificValueExpression(list, Constants.MONTH_MAP::get)),

  /** Expression representing specific ranges for months. */
  SPECIFIC_MONTH_RANGES(
      SpecificIntervalValidator.MONTH_INTERVAL_VALIDATOR::isValidMultiIntervalList,
      list -> Generators.rangeExpression(list, Constants.MONTH_MAP::get, RangeType.FLEXIBLE)),

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
