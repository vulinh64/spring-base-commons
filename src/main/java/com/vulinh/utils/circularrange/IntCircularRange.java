package com.vulinh.utils.circularrange;

import java.util.function.Function;

/** Circular range for Integer values. */
public interface IntCircularRange extends CircularRange<Integer> {

  @Override
  default Function<Integer, String> getTransformer() {
    return String::valueOf;
  }
}
