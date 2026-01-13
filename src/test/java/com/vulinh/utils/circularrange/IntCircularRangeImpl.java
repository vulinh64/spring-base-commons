package com.vulinh.utils.circularrange;

import java.util.List;
import java.util.stream.IntStream;

/** Test implementation of IntCircularRange for 0-23 (e.g., hours of the day). */
public record IntCircularRangeImpl(Integer start, Integer end) implements IntCircularRange {

  private static final List<Integer> ALL = IntStream.rangeClosed(0, 23).boxed().toList();

  public static IntCircularRangeImpl of(int start, int end) {
    return new IntCircularRangeImpl(start, end);
  }

  @Override
  public List<Integer> getAllElements() {
    return ALL;
  }
}
