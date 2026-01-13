package com.vulinh.utils.circularrange;

/**
 * Represents a segment (inclusive range) from {@code start} to {@code end} within a circular
 * sequence. Used for merging and comparing contiguous or wrapped intervals.
 *
 * @param start the start index (inclusive)
 * @param end the end index (inclusive)
 */
public record Segment(int start, int end) implements Comparable<Segment> {

  /**
   * Creates a new {@link Segment} instance.
   *
   * @param start the start index (inclusive)
   * @param end the end index (inclusive)
   * @return a new {@link Segment}
   */
  static Segment of(int start, int end) {
    return new Segment(start, end);
  }

  @Override
  public int compareTo(Segment other) {
    if (start != other.start) {
      return Integer.compare(start, other.start);
    }
    return Integer.compare(end, other.end);
  }

  /**
   * Checks if this segment overlaps with or is adjacent to another segment. Overlap means the two
   * segments share at least one index. Adjacency means the segments are next to each other (e.g.,
   * 1-2 and 3-4).
   *
   * @param other the other segment to check
   * @return {@code true} if the segments overlap or are adjacent, {@code false} otherwise
   */
  boolean isOverlapped(Segment other) {
    // Check for overlap: segments share at least one index
    // Check for adjacency: segments are consecutive (end + 1 == other.start or other.end + 1 ==
    // start)
    return Math.max(start, other.start) <= Math.min(end, other.end) + 1;
  }

  /**
   * Returns a new segment that is the union of this segment and another.
   *
   * @param other the other segment to merge with
   * @return a new merged segment covering both ranges
   */
  Segment merge(Segment other) {
    return new Segment(Math.min(start, other.start), Math.max(end, other.end));
  }
}
