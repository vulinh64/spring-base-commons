package com.vulinh.utils.circularrange;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
  public static <T> String mergeCircularRanges(List<? extends CircularRange<T>> ranges) {
    return mergeCircularRanges(ranges, MergeMode.NO_GAPS);
  }

  /**
   * Merges circular ranges with optional gap tolerance.
   *
   * @param ranges List of circular ranges to merge
   * @param mergeMode Merge mode defining gap tolerance
   * @return Merged range string representation
   * @param <T> Type of elements in the circular range
   */
  public static <T> String mergeCircularRanges(
      List<? extends CircularRange<T>> ranges, MergeMode mergeMode) {
    // Handle null or empty input
    if (ranges == null || ranges.isEmpty()) {
      return "";
    }

    // Get the cycle of elements and its length
    var elementCycle = ranges.get(0).getAllElements();
    var cycleLength = elementCycle.size();

    var rangeSegments = new LinkedList<Segment>();

    // Convert each range to one or two segments (handle wrap-around)
    for (var range : ranges) {
      var startIndex = elementCycle.indexOf(range.start());
      var endIndex = elementCycle.indexOf(range.end());

      // Normal range (including single-element ranges where start == end)
      if (startIndex <= endIndex) {
        rangeSegments.add(Segment.of(startIndex, endIndex));
      } else {
        // Wrapped range: split into two segments
        // e.g., SAT-MON becomes [SAT-SUN] and [MON-MON]
        rangeSegments.add(Segment.of(startIndex, cycleLength - 1));
        rangeSegments.add(Segment.of(0, endIndex));
      }
    }

    // Sort and merge overlapping segments
    var mergedSegments =
        mergeOverlappingSegments(rangeSegments.stream().sorted().toList(), cycleLength, mergeMode);

    return formatMergedSegmentsOutput(mergedSegments, elementCycle, cycleLength, mergeMode);
  }

  // Merge overlapping or adjacent segments based on gap flag
  private static List<Segment> mergeOverlappingSegments(
      List<Segment> rangeSegments, int cycleLength, MergeMode mergeMode) {
    var mergedSegments = new LinkedList<Segment>();

    for (var currentSegment : rangeSegments) {
      // If no overlap/adjacency, add as new segment; else, merge with previous
      if (mergedSegments.isEmpty()
          || !mergedSegments.peekLast().isOverlapped(currentSegment, mergeMode)) {
        mergedSegments.add(currentSegment);
      } else {
        var lastSegment = mergedSegments.removeLast();
        mergedSegments.add(lastSegment.merge(currentSegment));
      }
    }

    // Re-stitch wrap-around segments if needed
    if (mergedSegments.size() > 1) {
      var firstSegment = mergedSegments.peekFirst();
      var lastSegment = mergedSegments.peekLast();

      // Check if first segment starts at 0 and last segment ends at cycleLength-1
      // This indicates they might connect across the wrap-around boundary
      if (firstSegment != null
          && lastSegment != null
          && firstSegment.start() == 0
          && lastSegment.end() == cycleLength - 1) {

        // Check if they're adjacent across the boundary (based on gap flag)
        // With gaps allowed, don't auto-merge wrap-around
        boolean shouldMerge =
            mergeMode == MergeMode.NO_GAPS && lastSegment.end() + 1 == cycleLength;

        if (shouldMerge) {
          // Merge them into a wrap-around segment
          mergedSegments.removeFirst();
          mergedSegments.removeLast();
          mergedSegments.add(Segment.of(lastSegment.start(), firstSegment.end()));
        }
      }
    }

    return mergedSegments;
  }

  private static <T> String formatMergedSegmentsOutput(
      List<Segment> mergedSegments, List<T> elementCycle, int cycleLength, MergeMode mergeMode) {
    if (mergedSegments.isEmpty()) {
      return "";
    }

    // Only check for full coverage when gaps are NOT allowed
    if (mergeMode == MergeMode.NO_GAPS) {
      var totalCoveredLength = 0;

      // Calculate total covered length to check for full coverage
      for (var segment : mergedSegments) {
        totalCoveredLength += segment.length(cycleLength);
      }

      // If all elements are covered, return "*"
      if (totalCoveredLength >= cycleLength) {
        return "*";
      }
    }

    // Otherwise, format each segment as start-end
    return mergedSegments.stream()
        .map(
            segment ->
                "%s-%s"
                    .formatted(elementCycle.get(segment.start()), elementCycle.get(segment.end())))
        .collect(Collectors.joining(","));
  }
}
