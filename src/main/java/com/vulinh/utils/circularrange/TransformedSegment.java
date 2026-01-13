package com.vulinh.utils.circularrange;

/**
 * Represents a segment of a circular range with string boundaries.
 * <p>
 * This record holds the string representations of the start and end boundaries of a merged or transformed
 * circular range segment. It is typically produced as the result of merging operations on circular ranges
 * (such as days of the week, months, or hours), where the boundaries are converted to strings using a
 * transformer function.
 * <p>
 * Use {@link #from(Segment, CircularRange)} to create a TransformedSegment from a {@link Segment} and a
 * sample {@link CircularRange} instance, which provides the element cycle and transformer.
 *
 * <h2>Usage Example:</h2>
 * <pre>
 *   TransformedSegment seg = TransformedSegment.from(segment, circularRange);
 *   String start = seg.start();
 *   String end = seg.end();
 * </pre>
 *
 * @param start the string representation of the segment's start boundary
 * @param end the string representation of the segment's end boundary
 */
public record TransformedSegment(String start, String end) {

  /**
   * Creates a {@link TransformedSegment} from a {@link Segment} and a sample {@link CircularRange}.
   * <p>
   * The sample provides the element cycle and transformer to convert the segment's start and end indices
   * to their string representations.
   *
   * @param segment the segment to transform
   * @param sample a sample circular range providing the element cycle and transformer
   * @return a new {@link TransformedSegment} with string boundaries
   */
  public static <T> TransformedSegment from(Segment segment, CircularRange<T> sample) {
    var allElements = sample.getAllElements();
    var transformer = sample.getTransformer();

    return new TransformedSegment(
        transformer.apply(allElements.get(segment.start())),
        transformer.apply(allElements.get(segment.end())));
  }
}
