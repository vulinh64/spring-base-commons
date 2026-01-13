package com.vulinh.utils.circularrange;

import java.util.List;
import java.util.function.Function;

/**
 * Represents a circular range of elements of type {@code T}.
 * A circular range is defined by a start and end element, and a fixed cycle of possible elements.
 *
 * @param <T> the type of elements in the circular range
 */
public interface CircularRange<T> {

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
   * Returns the ordered list of all possible elements in the cycle for this range.
   * This list should be cached and unmodifiable.
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

  /**
   * Returns the size of the cycle (number of possible elements).
   *
   * @return the size of the cycle
   */
  default int getSize() {
    return getAllElements().size();
  }
}
