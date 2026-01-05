package com.vulinh.utils.intersectedrange;

import java.util.Comparator;
import java.util.Objects;

/**
 * Represents a range with a start (from) and end (to) value, along with an optional comparator for
 * custom comparison logic.
 *
 * @param <T> Type of the range boundaries
 */
public final class Range<T> {

  /** Start of the range */
  private final T from;

  /** End of the range */
  private final T to;

  /** Optional {@link Comparator} for custom comparison */
  private final Comparator<? super T> comparator;

  /**
   * Create a builder for Range.
   *
   * @return A {@link RangeBuilder} instance
   * @param <T> Type of the range boundaries, must be comparable
   */
  public static <T extends Comparable<? super T>> RangeBuilder<T> builder() {
    return new RangeBuilder<>(null);
  }

  /**
   * Create a builder for Range with a custom comparator.
   *
   * @param comparator Custom comparator for the range boundaries
   * @return A {@link RangeBuilder} instance
   * @param <T> Type of the range boundaries
   */
  public static <T> RangeBuilder<T> builder(Comparator<T> comparator) {
    return new RangeBuilder<>(comparator);
  }

  /**
   * Constructor for Range that ensures from is less than or equal to.
   *
   * @param from Start of the range
   * @param to End of the range
   * @param comparator Optional comparator for custom comparison
   */
  public Range(T from, T to, Comparator<? super T> comparator) {
    if (ComparatorUtils.compare(from, to, comparator) > 0) {
      var temp = from;
      from = to;
      to = temp;
    }

    this.from = from;
    this.to = to;
    this.comparator = comparator;
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

    return ComparatorUtils.compare(getFrom(), other.getTo(), comparator) < 0
        && ComparatorUtils.compare(getTo(), other.getFrom(), comparator) >= 0;
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

    var otherFrom = other.getFrom();
    var otherTo = other.getTo();

    return new Range<>(
        ComparatorUtils.compare(from, otherFrom, comparator) < 0 ? from : otherFrom,
        ComparatorUtils.compare(to, otherTo, comparator) > 0 ? to : otherTo,
        comparator);
  }

  // For the sake of equals and hashCode, make sure that
  // you have a Comparator instance than can be compared

  @Override
  public boolean equals(Object o) {
    return (o instanceof Range<?> range)
        && Objects.equals(from, range.getFrom())
        && Objects.equals(to, range.getTo())
        && Objects.equals(comparator, range.comparator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, comparator);
  }

  public T getFrom() {
    return from;
  }

  public T getTo() {
    return to;
  }

  public static class RangeBuilder<T> {

    T from;
    T to;

    final Comparator<T> comparator;

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
