package com.vulinh.utils.circularrange;

import java.util.function.Function;

/**
 * Circular range for Integer values (e.g., hours in a day, 0-23).
 *
 * <p>This interface extends {@link CircularRange} for {@link Integer} values, providing a default
 * string transformer that returns the string representation of the integer. Implementations should
 * define the cycle of valid integer values (such as 0-23 for hours, or any other bounded integer
 * range).
 *
 * <p>Example usage:
 *
 * <pre>
 *   IntCircularRange range = ...;
 *   String start = range.getTransformer().apply(range.start());
 * </pre>
 */
public interface IntCircularRange extends CircularRange<Integer> {

  @Override
  default Function<Integer, String> getTransformer() {
    return String::valueOf;
  }
}
