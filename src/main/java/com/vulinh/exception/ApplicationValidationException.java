package com.vulinh.exception;

import com.vulinh.utils.validator.ApplicationError;
import java.io.Serial;
import org.springframework.lang.NonNull;

/**
 * Represents an application-specific exception related to data validation errors.
 *
 * <p>Not to be confused with {@link jakarta.xml.bind.ValidationException}. Yes, really! Both
 * classes also share the same data types for its fields.
 *
 * @see ApplicationException
 */
public class ApplicationValidationException extends ApplicationException {

  @Serial private static final long serialVersionUID = -1L;

  /**
   * Constructs a new application exception with the specified details.
   *
   * @param message The error message for logging purposes - not to be displayed to clients
   * @param applicationError The application error object providing error code and display message
   * @param args Optional arguments for message interpolation in display messages
   */
  public ApplicationValidationException(
      String message, @NonNull ApplicationError applicationError, Object... args) {
    this(message, applicationError, null, args);
  }

  /**
   * Constructs a new application exception with the specified details, plus a root cause.
   *
   * @param message The error message for logging purposes - not to be displayed to clients
   * @param applicationError The application error object providing error code and display message
   * @param args Optional arguments for message interpolation in display messages
   * @param throwable The root cause of this exception
   */
  public ApplicationValidationException(
      String message,
      @NonNull ApplicationError applicationError,
      Throwable throwable,
      Object... args) {
    super(message, applicationError, throwable, args);
  }
}
