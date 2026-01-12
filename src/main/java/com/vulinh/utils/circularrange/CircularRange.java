package com.vulinh.utils.circularrange;

import java.util.List;

/**
 * Represents a circular range of elements of type T.
 *
 * @param <T> the type of elements in the circular range
 */
public interface CircularRange<T> {

  /**
   * Gets the start element of the circular range.
   *
   * @return start element
   */
  T start();

  /**
   * Gets the end element of the circular range.
   *
   * @return end element
   */
  T end();

  /**
   * Gets all possible elements in the circular range's cycle, in order. This should be cached,
   * perhaps by using a static final list.
   *
   * @return list of all elements in the cycle (should not be modified and be cached)
   */
  List<T> getAllElements();

  default int getSize() {
    return getAllElements().size();
  }
}
