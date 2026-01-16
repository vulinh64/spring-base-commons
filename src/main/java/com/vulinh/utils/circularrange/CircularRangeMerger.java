package com.vulinh.utils.circularrange;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class for merging circular ranges of any type.
 *
 * <p>This class provides static methods to merge lists of {@link CircularRange} objects, handling
 * ranges that may wrap around the end of a cycle (such as days of the week, months, or hours).
 * Adjacent or overlapping ranges are merged into the minimal set of non-overlapping segments.
 *
 * <p>The result is a list of {@link TransformedSegment} objects, which contain the string
 * representations of the merged range boundaries, as defined by the transformer of the input range
 * type.
 *
 * <h2>Example usage:</h2>
 *
 * <pre>
 *   List<CircularDayOfWeek> input = ...;
 *   List<TransformedSegment> merged = Merger.mergeCircularRanges(input);
 * </pre>
 */
public class CircularRangeMerger {

  // Prevent instantiation
  private CircularRangeMerger() {}

  /**
   * Merges a list of circular ranges into the minimal set of non-overlapping segments.
   *
   * <p>Adjacent or overlapping ranges are merged. Ranges that wrap around the end of the cycle
   * (e.g., FRIDAY to MONDAY for days of the week) are handled automatically.
   *
   * @param ranges List of circular ranges to merge (maybe null or empty)
   * @param <T> Type of elements in the circular range
   * @return List of merged range segments, with string representations of boundaries
   */
  public static <T extends Comparable<? super T>> List<TransformedSegment> mergeCircularRanges(
      List<? extends CircularRange<T>> ranges) {
    // Handle null or empty input
    if (ranges == null || ranges.isEmpty()) {
      return Collections.emptyList();
    }

    // Get the cycle of elements and its length from the first range
    var firstElement = ranges.stream().findFirst().orElseThrow();
    var elementCycle = firstElement.getAllElements();
    var cycleLength = elementCycle.size();

    // Convert each range to one or two segments (handle wrap-around)
    List<Segment> rangeSegments = new LinkedList<>();
    for (var range : ranges) {
      int startIndex = elementCycle.indexOf(range.start());
      int endIndex = elementCycle.indexOf(range.end());
      if (startIndex <= endIndex) {
        // Normal segment (no wrap-around)
        rangeSegments.add(Segment.of(startIndex, endIndex));
      } else {
        // Split wrapped range into two segments
        rangeSegments.add(Segment.of(startIndex, cycleLength - 1));
        rangeSegments.add(Segment.of(0, endIndex));
      }
    }

    // Merge overlapping or adjacent segments, then format output
    return mergeOverlappingSegments(rangeSegments, cycleLength).stream()
        .map(s -> TransformedSegment.from(s, firstElement))
        .toList();
  }

  /**
   * Merges a list of segments, combining any that overlap or are adjacent, and handling
   * wrap-around.
   *
   * <p>If the first and last segments connect around the cycle, they are merged into a single
   * wrapped segment.
   *
   * @param rangeSegments List of segments to merge
   * @param cycleLength Length of the cycle (number of possible values)
   * @return List of merged segments
   */
  private static List<Segment> mergeOverlappingSegments(
      List<Segment> rangeSegments, int cycleLength) {
    var mergedSegments = new LinkedList<Segment>();

    // Sort segments and merge any that overlap or are adjacent
    for (var currentSegment : rangeSegments.stream().sorted().toList()) {
      if (mergedSegments.isEmpty() || !mergedSegments.peekLast().isOverlapped(currentSegment)) {
        // No overlap/adjacency: add as new segment
        mergedSegments.add(currentSegment);
      } else {
        // Overlap/adjacency: merge with previous segment
        var lastSegment = mergedSegments.removeLast();
        mergedSegments.add(lastSegment.merge(currentSegment));
      }
    }

    // Handle wrap-around: merge first and last if they connect around the cycle
    if (mergedSegments.size() > 1) {
      var firstSegment = mergedSegments.getFirst();
      var lastSegment = mergedSegments.getLast();

      // If last ends at cycleLength-1 and first starts at 0, and they are adjacent, merge them
      if (lastSegment.end() == cycleLength - 1 && firstSegment.start() == 0) {
        mergedSegments.removeFirst();
        mergedSegments.removeLast();
        // Create wrapped segment: from lastSegment.start() wrapping to firstSegment.end()
        mergedSegments.add(Segment.of(lastSegment.start(), firstSegment.end()));
      }
    }

    return mergedSegments;
  }
}
