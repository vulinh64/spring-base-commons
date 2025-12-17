package com.vulinh.exception;

import com.vulinh.utils.validator.ApplicationError;
import java.io.Serial;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.NonNull;

/** Represents a base class for application-specific exceptions. */
public abstract class ApplicationException extends RuntimeException {

  @Serial private static final long serialVersionUID = 0L;

  /**
   * The application error associated with this exception, providing error code and localized
   * message capabilities.
   */
  protected final transient ApplicationError applicationError;

  /** Arguments for message interpolation when generating display messages. */
  protected final transient Object[] args;

  /**
   * Constructs a new application exception with the specified details.
   *
   * @param message The error message for logging purposes - not to be displayed to clients
   * @param applicationError The application error object providing error code and display message
   * @param args Optional arguments for message interpolation in display messages
   */
  protected ApplicationException(
      String message, @NonNull ApplicationError applicationError, Object... args) {
    this(message, applicationError, null, args);
  }

  /**
   * Constructs a new application exception with the specified details, plus a root cause.
   *
   * @param message The error message for logging purposes - not to be displayed to clients
   * @param applicationError The application error object providing error code and display message
   * @param args Optional arguments for message interpolation in display messages. Is ensured to
   *     never be null.
   * @param throwable The root cause of this exception
   */
  protected ApplicationException(
      String message,
      @NonNull ApplicationError applicationError,
      Throwable throwable,
      Object... args) {
    super(message, throwable);
    this.applicationError = applicationError;
    this.args = args == null ? ArrayUtils.EMPTY_OBJECT_ARRAY : args;
  }

  public ApplicationError getApplicationError() {
    return applicationError;
  }

  public Object[] getArgs() {
    return args;
  }
}
