package com.vulinh.factory;

import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;

/** Some common {@link Predicate} for validation chains. */
public class ValidatorStepFactory {

  private ValidatorStepFactory() {
    throw new UnsupportedOperationException("No instantiation");
  }

  public static <T> Predicate<T> noBlankField(Function<T, String> extractor) {
    return dto -> StringUtils.isNotBlank(extractor.apply(dto));
  }

  public static <T> Predicate<T> noExceededLength(Function<T, String> extractor, int length) {
    return dto -> StringUtils.length(extractor.apply(dto)) <= length;
  }
}
