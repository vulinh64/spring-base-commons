package com.vulinh.utils.intersectedrange;

import java.util.Comparator;

/** Utility class for comparing objects with optional custom comparator. */
class ComparatorUtils {

  private ComparatorUtils() {}

  /**
   * Compare two objects using the provided comparator or their natural ordering. Handle edge cases,
   * like object not comparable or null values.
   *
   * @param firstObject First object
   * @param secondObject Second object
   * @param comparator Custom comparator, can be null
   * @return Comparison result: negative if o1 < o2, zero if o1 == o2, positive if o1 > o2
   * @param <T> Type of the objects being compared
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  static <T> int compare(T firstObject, T secondObject, Comparator<? super T> comparator) {
    if (firstObject == null || secondObject == null) {
      throw new IntersectedRangeException("Cannot compare null values");
    }

    if (comparator != null) {
      return comparator.compare(firstObject, secondObject);
    }

    if (!(firstObject instanceof Comparable comparable)) {
      throw new IntersectedRangeException(
          "Type %s is not comparable".formatted(firstObject.getClass().getName()));
    }

    return comparable.compareTo(secondObject);
  }
}
