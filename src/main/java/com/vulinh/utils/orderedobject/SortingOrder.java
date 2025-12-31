package com.vulinh.utils.orderedobject;

import java.util.Comparator;

/** Enum representing the direction of sorting operations. */
public enum SortingOrder {

  /** Sort elements in ascending order. */
  ASCENDING,

  /** Sort elements in descending order. */
  DESCENDING;

  /**
   * Returns a {@link Comparator} that sorts in the direction specified by this enum value.
   *
   * @return a {@link Comparator} for the specified direction
   * @param <U> the type of objects being compared, must be Comparable
   */
  <U extends Comparable<? super U>> Comparator<U> toDirection() {
    return this == ASCENDING ? Comparator.naturalOrder() : Comparator.reverseOrder();
  }

  /**
   * Returns a {@link Comparator} that sorts {@link OrderedObject} instances by their order field in
   * the direction specified by this enum value.
   *
   * @return a {@link Comparator} for {@link OrderedObject} instances based on their order field
   * @param <U> the type of objects wrapped in {@link OrderedObject}
   */
  <U> Comparator<OrderedObject<U>> toOrderComparator() {
    return this == ASCENDING
        ? Comparator.comparingInt(OrderedObject::order)
        : Comparator.<OrderedObject<U>>comparingInt(OrderedObject::order).reversed();
  }
}
