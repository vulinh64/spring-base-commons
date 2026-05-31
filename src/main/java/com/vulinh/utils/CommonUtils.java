package com.vulinh.utils;

import java.util.function.Supplier;

/**
 * Provides a set of shared utility methods for common operations across the application. This class
 * encapsulates various helper functions to promote code reusability and maintainability.
 */
public class CommonUtils {

  public static final String FROM_TO = "%s-%s";

  public static final String COMMA = ",";

  private CommonUtils() {}

  /**
   * Retrieves the first three letters of an enum's name.
   *
   * @param value The enum value.
   * @return A {@code String} representing the first three letters of the enum's name.
   */
  public static String first3Letters(Enum<?> value) {
    return value.name().substring(0, 3);
  }

  /**
   * Checks if the given object is null. If it is, a runtime exception supplied by the {@code
   * exceptionSupplier} is thrown. Otherwise, the object is returned. This method is useful for
   * validating method arguments or ensuring non-null states.
   *
   * @param object The object to check for nullability.
   * @param exceptionSupplier A supplier that provides a {@link RuntimeException} to be thrown if
   *     the object is null.
   * @param <T> The type of the object.
   * @return The object itself if it is not null.
   * @throws RuntimeException if the object is null, as provided by the {@code exceptionSupplier}.
   * @see java.util.Objects#requireNonNull(Object)
   */
  public static <T> T throwsIfNull(
      T object, Supplier<? extends RuntimeException> exceptionSupplier) {
    if (object == null) {
      throw exceptionSupplier.get();
    }

    return object;
  }
}
