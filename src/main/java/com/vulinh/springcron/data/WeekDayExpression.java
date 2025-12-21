package com.vulinh.springcron.data;

import com.vulinh.springcron.PartExpression;
import com.vulinh.springcron.RangeType;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/** Enumeration representing different expressions for days of the week in a cron expression. */
public enum WeekDayExpression implements PartExpression {

  /** Expression representing every day of the week. */
  EVERY_WEEK_DAY(Validators.alwaysTrue(), Generators.every()),

  /** Expression representing every N days of the week. */
  EVERY_N_WEEK_DAY(
      list ->
          Validators.isValidSingletonListWithinBounds(
              list, Constants.DAY_OF_WEEK_MIN, Constants.DAY_OF_WEEK_MAX),
      Generators::everyNthExpression),

  /** Expression representing a range between two days of the week. */
  BETWEEN_WEEK_DAYS(
      list ->
          Validators.isValidDualListWithinBounds(
              list, Constants.DAY_OF_WEEK_MIN, Constants.DAY_OF_WEEK_MAX),
      list ->
          Generators.betweenExpression(list, RangeType.FLEXIBLE, Constants.DAY_OF_WEEK_MAP::get)),

  /** Expression representing specific values for days of the week. */
  SPECIFIC_WEEK_DAYS(
      list ->
          Validators.isValidListWithinRange(
              list, Constants.DAY_OF_WEEK_MIN, Constants.DAY_OF_WEEK_MAX),
      list -> Generators.specificValueExpression(list, Constants.DAY_OF_WEEK_MAP::get)),

  /** Expression representing specific ranges for days of the week. */
  NTH_OCCURRENCE(
      list ->
          Validators.isNotEmpty(list)
              && list.size() >= 2
              && Validators.isBetweenInclusive(
                  list.get(0), Constants.DAY_OF_WEEK_MIN, Constants.DAY_OF_WEEK_MAX)
              && Validators.isBetweenInclusive(
                  list.get(1), Constants.MIN_NTH_OCCURRENCE, Constants.MAX_NTH_OCCURRENCE),
      list -> "%s#%s".formatted(Constants.DAY_OF_WEEK_MAP.get(list.get(0)), list.get(1))),

  /** Expression representing the last occurrence of a specific day of the week in a month. */
  LAST_OF_MONTH(
      list ->
          Validators.isValidSingletonListWithinBounds(
              list, Constants.DAY_OF_WEEK_MIN, Constants.DAY_OF_WEEK_MAX),
      list -> "%sL".formatted(Constants.DAY_OF_WEEK_MAP.get(list.get(0)))),

  /** Expression representing no specific care for day of weeks. */
  WEEK_DAY_NO_CARE(Validators.alwaysTrue(), Generators.noCare());

  final Predicate<List<Integer>> predicate;
  final Function<List<Integer>, String> generator;

  WeekDayExpression(Predicate<List<Integer>> predicate, Function<List<Integer>, String> generator) {
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
