package com.vulinh.utils.intersectedrange;

import static com.vulinh.utils.intersectedrange.IntersectedRangeMerger.mergeRanges;
import static com.vulinh.utils.intersectedrange.Range.builder;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Comparator;
import org.junit.jupiter.api.Test;

class CircularRangeIntersectedRangeMergerComparatorTest {

  static final Comparator<Character> COMPARATOR = Comparator.comparing(Character::toLowerCase);

  @Test
  void testMergeRangesWithComparator() {
    var mergedRanges =
        mergeRanges(
            of(
                builder(COMPARATOR).from('a').to('d').build(),
                builder(COMPARATOR).from('C').to('f').build(),
                builder(COMPARATOR).from('k').to('m').build(),
                builder(COMPARATOR).from('p').to('q').build(),
                builder(COMPARATOR).from('r').to('t').build()),
            COMPARATOR);

    assertEquals(4, mergedRanges.size());

    assertAll(
        () -> {
          var range1 = mergedRanges.get(0);

          assertEquals('a', range1.getFrom());
          assertEquals('f', range1.getTo());
        },
        () -> {
          var range1 = mergedRanges.get(1);

          assertEquals('k', range1.getFrom());
          assertEquals('m', range1.getTo());
        },
        () -> {
          var range3 = mergedRanges.get(2);

          assertEquals('p', range3.getFrom());
          assertEquals('q', range3.getTo());
        },
        () -> {
          var range4 = mergedRanges.get(3);

          assertEquals('r', range4.getFrom());
          assertEquals('t', range4.getTo());
        });
  }
}
