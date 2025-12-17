package com.vulinh.utils.validator;

import java.util.function.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * A single, reusable validation rule for objects of type {@code T}.
 *
 * <p>A {@code ValidatorStep} encapsulates:
 *
 * <ul>
 *   <li>a {@link Predicate} to evaluate an input,
 *   <li>an {@link ApplicationError} that identifies the failure when the predicate evaluates to
 *       {@code false},
 *   <li>optional {@link #getArgs() arguments} used to format or internationalize the error message,
 *       and
 *   <li>a developer-facing {@link #getExceptionMessage() message} suitable for logs or exception
 *       messages.
 * </ul>
 *
 * <p>Semantics and expectations:
 *
 * <ul>
 *   <li>The predicate should return {@code true} when the value is valid; {@code false} indicates a
 *       validation failure.
 *   <li>Implementations should be side-effect-free and ideally thread-safe so they can be reused.
 *   <li>Null handling is implementation-defined; document your predicate's behavior if {@code null}
 *       values are expected.
 * </ul>
 *
 * @param <T> the type being validated
 * @see Predicate
 * @see ApplicationError
 */
public interface ValidatorStep<T> {

  /**
   * The predicate that determines whether the provided value is valid.
   *
   * <p>Returns {@code true} if the value passes validation; {@code false} to indicate a validation
   * failure that should be mapped to {@link #getApplicationError()}.
   *
   * @return a non-null {@link Predicate} to evaluate inputs of type {@code T}
   */
  @NonNull
  Predicate<T> getPredicate();

  /**
   * The error descriptor used when {@link #getPredicate()} evaluates to {@code false}.
   * Implementations typically return a stable error code suitable for i18n translation.
   *
   * @return a non-null {@link ApplicationError}
   */
  @NonNull
  ApplicationError getApplicationError();

  /**
   * Optional positional arguments to be used when rendering the {@link ApplicationError} (for
   * example, with an i18n message source). If no arguments are required, implementations may return
   * an empty array. Can be null.
   *
   * @return the argument array
   */
  @Nullable
  default Object[] getArgs() {
    return ArrayUtils.EMPTY_OBJECT_ARRAY;
  }

  /**
   * A developer-oriented message describing the validation failure, suitable for logs or as an
   * exception message. This is usually not localized.
   *
   * @return the message to use when throwing/logging on failure; may be {@code null} if not
   *     applicable
   */
  @NonNull
  String getExceptionMessage();
}
