package com.vulinh.utils.circularrange;

import java.util.List;
import java.util.function.Function;

/**
 * Represents a circular range of elements of type {@code T}.
 *
 * <p>A circular range is defined by a start and end element, and a fixed cycle of possible
 * elements. This allows for ranges that may wrap around the end of the cycle (e.g., for days of the
 * week, a range from FRIDAY to MONDAY).
 *
 * <p>Implementations should provide an unmodifiable, ordered list of all possible elements in the
 * cycle, and a transformer function for string representation.
 *
 * @param <T> the type of elements in the circular range, should be comparable (a circular range
 *     still need a start and an end)
 */
public interface CircularRange<T extends Comparable<? super T>> {

  /**
   * Returns the start element of the circular range.
   *
   * @return the start element
   */
  T start();

  /**
   * Returns the end element of the circular range.
   *
   * @return the end element
   */
  T end();

  /**
   * Returns the ordered list of all possible elements in the cycle for this range. This list should
   * be cached and unmodifiable.
   *
   * @return the list of all elements in the cycle (should not be modified)
   */
  List<T> getAllElements();

  /**
   * Returns a transformer function to convert elements to their string representation.
   *
   * @return a function that transforms an element to a string
   */
  Function<T, String> getTransformer();
}
