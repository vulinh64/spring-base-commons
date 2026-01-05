package com.vulinh.utils.intersectedrange;

import java.util.*;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

/** Utility class for merging intersected ranges. */
public class Merger {

  private Merger() {}

  /**
   * Merge intersected ranges with comparable type.
   *
   * @param ranges list of ranges to be merged
   * @return list of merged ranges
   * @param <T> the type of the range boundaries, must be comparable
   */
  public static <T extends Comparable<? super T>> List<Range<T>> mergeRanges(
      List<Range<T>> ranges) {
    return mergeRanges(ranges, null);
  }

  /**
   * Merge intersected ranges with a custom comparator.
   *
   * @param ranges list of ranges to be merged
   * @param comparator custom comparator for the range boundaries
   * @return list of merged ranges
   * @param <T> the type of the range boundaries
   */
  public static <T> List<Range<T>> mergeRanges(
      List<Range<T>> ranges, @Nullable Comparator<T> comparator) {
    if (CollectionUtils.isEmpty(ranges)) {
      return Collections.emptyList();
    }

    // Create a sortable copy of the input ranges
    // Because the input may or may not be mutable
    var sortableRanges = new ArrayList<>(ranges);

    sortableRanges.sort(
        (o1, o2) -> ComparatorUtils.compare(o1.getFrom(), o2.getFrom(), comparator));

    // We don't want to see array reallocation during merging
    var mergedRanges = new LinkedList<Range<T>>();

    var currentRange = sortableRanges.get(0);

    for (var i = 1; i < sortableRanges.size(); i++) {
      var nextRange = sortableRanges.get(i);

      if (currentRange.isIntersected(nextRange)) {
        currentRange = currentRange.merge(nextRange);
      } else {
        mergedRanges.add(currentRange);
        currentRange = nextRange;
      }
    }

    mergedRanges.add(currentRange);

    // Return an unmodifiable copy of the merged ranges
    // Quite expensive because we are doing pointer chasing here
    // But acceptable because we don't use ArrayList with dynamic size
    return List.copyOf(mergedRanges);
  }
}
