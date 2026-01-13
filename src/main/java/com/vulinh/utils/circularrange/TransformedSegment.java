package com.vulinh.utils.circularrange;

public record TransformedSegment(String start, String end) {

  public static <T> TransformedSegment from(Segment segment, CircularRange<T> sample) {
    var allElements = sample.getAllElements();
    var transformer = sample.getTransformer();

    return new TransformedSegment(
        transformer.apply(allElements.get(segment.start())),
        transformer.apply(allElements.get(segment.end())));
  }
}
