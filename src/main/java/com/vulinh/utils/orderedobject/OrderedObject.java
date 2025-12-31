package com.vulinh.utils.orderedobject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * A record that holds an object of generic type T along with an order value. This class provides
 * functionality to track the original position of objects in a collection while allowing for custom
 * sorting operations. This class ensures that the wrapped value is never null and the order is
 * never negative.
 *
 * @param value the object being wrapped
 * @param order the order value associated with the object
 * @param <T> the type of object being wrapped
 */
public record OrderedObject<T>(T value, int order) {

  /**
   * Utility class providing static methods for creating and manipulating {@link OrderedObject}
   * instances.
   */
  public static class Wrapper {

    private Wrapper() {}

    /**
     * Creates a new {@link OrderedObject} that wraps the given object with the specified order.
     *
     * @param object the object to wrap, must not be null
     * @param order the order value to assign
     * @return a new {@link OrderedObject} containing the given object and order
     * @param <T> the type of the object to wrap
     */
    public static <T> OrderedObject<T> wrap(T object, int order) {
      return new OrderedObject<>(object, order);
    }

    /**
     * Creates a {@link Comparator} that first compares {@link OrderedObject} instances by their
     * natural order using the provided key extractor function, then by their order value. This
     * method uses ascending order and places null values last.
     *
     * @param keyExtractor a function to extract the comparison key from the wrapped object
     * @return a {@link Comparator} for {@link OrderedObject} instances
     * @param <T> the type of objects wrapped in {@link OrderedObject}
     * @param <U> the type of the key used for comparison, must be {@link Comparable}
     */
    public static <T, U extends Comparable<? super U>>
        Comparator<OrderedObject<T>> firstCompareByNaturalOrder(Function<T, U> keyExtractor) {
      return firstCompareBy(keyExtractor, SortingOrder.ASCENDING, NullsOrder.NULLS_LAST);
    }

    /**
     * Creates a {@link Comparator} that first compares {@link OrderedObject} instances using the
     * provided key extractor function, then by their order value. The comparison order and null
     * handling strategy can be specified.
     *
     * @param keyExtractor a function to extract the comparison key from the wrapped object, must
     *     not be null
     * @param sortingOrder the sorting order (ascending or descending), must not be null
     * @param nullsOrder the strategy for handling null values, must not be null
     * @return a {@link Comparator} for {@link OrderedObject} instances
     * @param <T> the type of objects wrapped in {@link OrderedObject}
     * @param <U> the type of the key used for comparison, {@link Comparable}
     * @throws NullPointerException if any parameter is null
     */
    public static <T, U extends Comparable<? super U>> Comparator<OrderedObject<T>> firstCompareBy(
        Function<T, U> keyExtractor, SortingOrder sortingOrder, NullsOrder nullsOrder) {
      Objects.requireNonNull(keyExtractor, "Object extractor function cannot be null");
      Objects.requireNonNull(sortingOrder, "Order direction cannot be null");
      Objects.requireNonNull(nullsOrder, "Nulls order strategy cannot be null");

      return Comparator.<OrderedObject<T>, U>comparing(
              wrapper -> keyExtractor.apply(wrapper.value()),
              nullsOrder.<U>toNullsOrder(sortingOrder.toDirection()))
          .thenComparing(sortingOrder.toOrderComparator());
    }

    /**
     * Creates a sorted list from the original list based on the provided key extractor and sorting
     * preferences. The original order of elements is preserved for elements that compare as equal.
     *
     * @param originalList the list to sort, must not contain null elements
     * @param keyExtractor a function to extract the comparison key from each element
     * @param sortingOrder the sorting order (ascending or descending)
     * @param nullsOrder the strategy for handling null keys
     * @return a new sorted list containing all elements from the original list
     * @param <T> the type of elements in the list
     * @param <U> the type of the key used for comparison, must be {@link Comparable}
     */
    public static <T, U extends Comparable<? super U>> List<T> toSortedList(
        List<T> originalList,
        Function<T, U> keyExtractor,
        SortingOrder sortingOrder,
        NullsOrder nullsOrder) {
      if (originalList == null || originalList.isEmpty()) {
        return Collections.emptyList();
      }

      Objects.requireNonNull(keyExtractor, "Key extractor cannot be null");
      Objects.requireNonNull(sortingOrder, "Order cannot be null");
      Objects.requireNonNull(nullsOrder, "Nulls order cannot be null");

      return IntStream.range(0, originalList.size())
          .mapToObj(index -> wrapNonNull(originalList, index))
          .sorted(firstCompareBy(keyExtractor, sortingOrder, nullsOrder))
          .map(OrderedObject::unwrap)
          .toList();
    }

    static <T> OrderedObject<T> wrapNonNull(List<T> originalList, int index) {
      var value = originalList.get(index);

      if (value == null) {
        throw new IllegalArgumentException("List cannot contain any null object");
      }

      return wrap(value, index);
    }
  }

  /**
   * Creates a new {@link OrderedObject} with the specified value and order.
   *
   * @param value the value to store, must not be null
   * @param order the order value, must not be negative
   */
  public OrderedObject {
    if (value == null) {
      throw new IllegalArgumentException("Object cannot be null");
    }

    if (order < 0) {
      throw new IllegalArgumentException("Order cannot be negative");
    }
  }

  /**
   * Unwraps and returns the value stored in this {@link OrderedObject}.
   *
   * @return the wrapped value, never {@code null}
   */
  public T unwrap() {
    return value;
  }
}
