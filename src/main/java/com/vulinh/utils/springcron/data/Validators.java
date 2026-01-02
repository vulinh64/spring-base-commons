package com.vulinh.utils.springcron.data;

import java.util.List;
import java.util.function.Predicate;

/** Utility class providing common predicates for cron expression handling. */
class Validators {

  private Validators() {}

  /**
   * A predicate that always returns true, regardless of the input.
   *
   * @param <T> The type of the input parameter (not used).
   * @return The predicate that always returns true.
   */
  static <T> Predicate<T> alwaysTrue() {
    return ignored -> true;
  }

  /**
   * Checks if a list is valid: not null, not empty. The way the data is provided ensures
   * non-nullity.
   *
   * @param list The list to validate.
   * @return The predicate that checks for a valid list.
   */
  static boolean isNotEmpty(List<?> list) {
    return !list.isEmpty();
  }

  /**
   * Validates if a list is a valid singleton list with its element within specified bounds.
   *
   * @param list the list to validate.
   * @param lowerBound Floor bound
   * @param upperBound Ceiling bound
   * @return {@code true} if the list is a valid singleton list within bounds; {@code false}
   *     otherwise.
   */
  static boolean isValidSingletonListWithinBounds(
      List<Integer> list, int lowerBound, int upperBound) {
    return isNotEmpty(list) && isBetweenInclusive(list.get(0), lowerBound, upperBound);
  }

  /**
   * Validates if a list is a valid dual-element list with both elements within specified bounds.
   *
   * @param lowerBound Floor bound (inclusive).
   * @param upperBound Ceiling bound (inclusive).
   * @return {@code true} if the list is a valid dual-element list within bounds; {@code false}
   */
  static boolean isValidDualListWithinBounds(List<Integer> list, int lowerBound, int upperBound) {
    return isValidSingletonListWithinBounds(list, lowerBound, upperBound)
        && list.size() >= 2
        && isBetweenInclusive(list.get(1), lowerBound, upperBound);
  }

  /**
   * Validates if a list is valid and all its elements are within specified bounds.
   *
   * @param lowerBound Floor bound (inclusive).
   * @param upperBound Ceiling bound (inclusive).
   * @return {@code true} if the list is valid and all elements are within bounds; {@code false}
   */
  static boolean isValidListWithinRange(List<Integer> list, int lowerBound, int upperBound) {
    return isNotEmpty(list)
        && list.stream().allMatch(e -> isBetweenInclusive(e, lowerBound, upperBound));
  }

  /**
   * Checks if a value is between the specified lower and upper bounds, inclusive.
   *
   * @param value The value to check.
   * @param lowerBound Floor bound
   * @param upperBound Ceiling bound
   * @return {@code true} if the value is between the bounds; {@code false} otherwise.
   */
  static boolean isBetweenInclusive(int value, int lowerBound, int upperBound) {
    return value >= lowerBound && value <= upperBound;
  }
}
