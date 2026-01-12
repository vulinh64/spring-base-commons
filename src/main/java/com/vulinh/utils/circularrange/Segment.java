package com.vulinh.utils.circularrange;

/**
 * Segment representing a range from start to end (inclusive).
 *
 * @param start Start value
 * @param end End value
 */
record Segment(int start, int end) implements Comparable<Segment> {

  /**
   * Factory method to create a {@link Segment} instance.
   *
   * @param start Start value
   * @param end End value
   * @return new {@link Segment} instance
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
   * Checks if this segment overlaps with another segment based on the specified merge mode.
   *
   * @param other Other segment to check overlap with
   * @param mode Gap tolerance mode
   * @return {@code true} if segments overlap or are adjacent based on mode, {@code false} if
   *     otherwise
   */
  boolean isOverlapped(Segment other, MergeMode mode) {
    // Check for overlap: segments share at least one index
    var overlaps = Math.max(start, other.start) <= Math.min(end, other.end);

    return mode == MergeMode.ALLOW_GAPS
        // With gaps allowed, only actual overlap counts
        ? overlaps
        // Without gaps (NO_GAPS), adjacent segments should also merge
        // e.g., Segment(0,4) and Segment(5,6) where 4+1=5
        : overlaps || (end + 1 == other.start) || (other.end + 1 == start);
  }

  /**
   * Merges this segment with another segment.
   *
   * @param other Other segment to merge with
   * @return New merged segment
   */
  Segment merge(Segment other) {
    return new Segment(Math.min(start, other.start), Math.max(end, other.end));
  }

  int length(int modulus) {
    if (start == -1 && end == modulus) {
      return modulus;
    }

    // Normal case: end >= start (includes single element case where start == end)
    if (end >= start) {
      // Segment represents inclusive range [start, end]
      // Segment(0,4) covers: 0,1,2,3,4 = 5 elements
      // Segment(6,6) covers: 6 = 1 element (single day)
      return end - start + 1;
    }

    // Wrapped case: from start to end of cycle, then from beginning to end
    // Example: Segment(10, 2) with modulus 12
    // Covers: 10,11,0,1,2 = (12-10) + 2 + 1 = 5 elements
    return (modulus - start) + end + 1;
  }
}
