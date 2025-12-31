package com.vulinh.utils.orderedobject;

import java.util.Comparator;

/** Enum representing strategies for handling null values during comparison operations. */
public enum NullsOrder {

  /** Place null values before non-null values in sorting results. */
  NULLS_FIRST,

  /** Place null values after non-null values in sorting results. */
  NULLS_LAST,

  /** Reject null values with an exception during comparison. */
  NULLS_HOSTILE;

  /**
   * Transforms a {@link Comparator} according to the null-handling strategy.
   *
   * @param comparator the base {@link Comparator} to transform
   * @return a {@link Comparator} with the appropriate null-handling behavior
   * @param <U> the type of objects being compared
   */
  <U> Comparator<U> toNullsOrder(Comparator<U> comparator) {
    return switch (this) {
      case NULLS_FIRST -> Comparator.nullsFirst(comparator);
      case NULLS_LAST -> Comparator.nullsLast(comparator);
      default ->
          (left, right) -> {
            if (left == null || right == null) {
              throw new IllegalArgumentException("Null values not allowed!");
            }

            return comparator.compare(left, right);
          };
    };
  }
}
