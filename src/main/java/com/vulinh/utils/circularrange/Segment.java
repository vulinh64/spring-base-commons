package com.vulinh.utils.circularrange;

/**
 * Represents a segment (inclusive range) from {@code start} to {@code end} within a circular
 * sequence.
 *
 * <p>Segments are used for merging and comparing contiguous or wrapped intervals in a circular
 * structure (such as days of the week, hours in a day, or months in a year). Each segment is
 * defined by its start and end indices (inclusive), which refer to positions within the cycle.
 *
 * <p>This record provides utility methods for creating, comparing, merging, and checking
 * overlap/adjacency of segments. It is a key building block for algorithms that merge circular
 * ranges into minimal sets of non-overlapping intervals.
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>
 *   Segment s1 = Segment.of(2, 5);
 *   Segment s2 = Segment.of(6, 8);
 *   boolean overlap = s1.isOverlapped(s2);
 *   Segment merged = s1.merge(s2);
 * </pre>
 *
 * @param start the start index (inclusive) within the cycle
 * @param end the end index (inclusive) within the cycle
 */
public record Segment(int start, int end) implements Comparable<Segment> {

  /**
   * Creates a new {@link Segment} instance with the given start and end indices.
   *
   * @param start the start index (inclusive)
   * @param end the end index (inclusive)
   * @return a new {@link Segment}
   */
  static Segment of(int start, int end) {
    return new Segment(start, end);
  }

  /**
   * Compares this segment to another by start index, then by end index. Used for sorting segments
   * in merge algorithms.
   */
  @Override
  public int compareTo(Segment other) {
    return start != other.start
        ? Integer.compare(start, other.start)
        : Integer.compare(end, other.end);
  }

  /**
   * Checks if this segment overlaps with or is adjacent to another segment.
   *
   * <p>Overlap means the two segments share at least one index. Adjacency means the segments are
   * next to each other (e.g., 1-2 and 3-4).
   *
   * @param other the other segment to check
   * @return {@code true} if the segments overlap or are adjacent, {@code false} otherwise
   */
  boolean isOverlapped(Segment other) {
    // Overlap: segments share at least one index
    // Adjacency: segments are consecutive (end + 1 == other.start or other.end + 1 == start)
    return Math.max(start, other.start) <= Math.min(end, other.end) + 1;
  }

  /**
   * Returns a new segment that is the union of this segment and another.
   *
   * <p>The merged segment covers the full range from the minimum start to the maximum end of both
   * segments.
   *
   * @param other the other segment to merge with
   * @return a new merged segment covering both ranges
   */
  Segment merge(Segment other) {
    return new Segment(Math.min(start, other.start), Math.max(end, other.end));
  }
}
