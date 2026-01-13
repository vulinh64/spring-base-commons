package com.vulinh.utils.circularrange;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/** Utility class for merging circular ranges. */
public class Merger {

  private Merger() {}

  /**
   * Merges circular ranges with no gap tolerance (adjacent ranges will merge).
   *
   * @param ranges List of circular ranges to merge
   * @return Merged range string representation
   * @param <T> Type of elements in the circular range
   */
  public static <T> List<TransformedSegment> mergeCircularRanges(
      List<? extends CircularRange<T>> ranges) {
    // Handle null or empty input
    if (ranges == null || ranges.isEmpty()) {
      return Collections.emptyList();
    }

    var firstElement = ranges.stream().findFirst().orElseThrow();

    var elementCycle = firstElement.getAllElements();
    var cycleLength = firstElement.getSize();

    // Convert each range to one or two segments (handle wrap-around)
    List<Segment> rangeSegments = new LinkedList<>();
    for (var range : ranges) {
      int startIndex = elementCycle.indexOf(range.start());
      int endIndex = elementCycle.indexOf(range.end());
      if (startIndex <= endIndex) {
        rangeSegments.add(Segment.of(startIndex, endIndex));
      } else {
        rangeSegments.add(Segment.of(startIndex, cycleLength - 1));
        rangeSegments.add(Segment.of(0, endIndex));
      }
    }

    // Sort and merge overlapping segments, then format output
    return mergeOverlappingSegments(rangeSegments, cycleLength).stream()
        .map(s -> TransformedSegment.from(s, firstElement))
        .toList();
  }

  // Merge overlapping segments and handle circular wrap-around
  private static List<Segment> mergeOverlappingSegments(
      List<Segment> rangeSegments, int cycleLength) {
    var mergedSegments = new LinkedList<Segment>();

    for (var currentSegment : rangeSegments.stream().sorted().toList()) {
      // If no overlap/adjacency, add as new segment; else, merge with previous
      if (mergedSegments.isEmpty() || !mergedSegments.peekLast().isOverlapped(currentSegment)) {
        mergedSegments.add(currentSegment);
      } else {
        var lastSegment = mergedSegments.removeLast();
        mergedSegments.add(lastSegment.merge(currentSegment));
      }
    }

    // Check if first and last segments connect around the circle (wrap-around merge)
    if (mergedSegments.size() > 1) {
      var firstSegment = mergedSegments.getFirst();
      var lastSegment = mergedSegments.getLast();

      // If last segment ends at cycleLength-1 and first segment starts at 0,
      // and they are adjacent, merge them into a wrapped segment
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
