package com.vulinh.utils.intersectedrange;

import java.util.Comparator;
import org.springframework.lang.Nullable;

/**
 * Represents a range with a start (from) and end (to) value, along with an optional comparator for
 * custom comparison logic.
 *
 * @param from Start of the range
 * @param to End of the range
 * @param comparator Optional comparator for custom comparison
 * @param <T> Type of the range boundaries
 */
public record Range<T>(T from, T to, @Nullable Comparator<T> comparator) {

  /**
   * Compact constructor for Range that ensures from is less than or equal to.
   *
   * @param from Start of the range
   * @param to End of the range
   * @param comparator Optional comparator for custom comparison
   */
  public Range {
    if (ComparatorUtils.compare(from, to, comparator) > 0) {
      var temp = from;
      from = to;
      to = temp;
    }
  }

  /**
   * Check if this range intersects with another range.
   *
   * @param other Other range to check intersection with
   * @return {@code true} if the ranges intersect, {@code false} if otherwise
   */
  public boolean isIntersected(Range<T> other) {
    if (other == null) {
      throw new IntersectedRangeException("Other range cannot be null");
    }

    return ComparatorUtils.compare(from(), other.to(), comparator) < 0
        && ComparatorUtils.compare(to(), other.from(), comparator) >= 0;
  }

  /**
   * Create a new range that is the merger of this range and another intersected range.
   *
   * <p>For example, given two ranges [1, 5] and [4, 8], the merged range would be [1, 8].
   *
   * @param other Other range to merge with
   * @return New merged range
   * @throws IntersectedRangeException if the ranges do not intersect, or if the {@code other} range
   *     is invalid.
   */
  public Range<T> merge(Range<T> other) {
    if (!isIntersected(other)) {
      throw new IntersectedRangeException("Cannot merge non-intersected ranges");
    }

    return new Range<>(
        ComparatorUtils.compare(from(), other.from(), comparator) < 0 ? from() : other.from(),
        ComparatorUtils.compare(to(), other.to(), comparator) > 0 ? to() : other.to(),
        comparator);
  }

  public static <T extends Comparable<? super T>> RangeBuilder<T> builder() {
    return new RangeBuilder<>(null);
  }

  public static <T> RangeBuilder<T> builder(Comparator<T> comparator) {
    return new RangeBuilder<>(comparator);
  }

  public static class RangeBuilder<T> {

    T from;
    T to;

    private final Comparator<T> comparator;

    RangeBuilder(Comparator<T> comparator) {
      this.comparator = comparator;
    }

    public RangeBuilder<T> from(T from) {
      this.from = from;
      return this;
    }

    public RangeBuilder<T> to(T to) {
      this.to = to;
      return this;
    }

    public Range<T> build() {
      return new Range<>(from, to, comparator);
    }
  }
}
