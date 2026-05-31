package com.vulinh.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vulinh.data.type.CommonServiceCodeError;
import com.vulinh.exception.ApplicationException;
import com.vulinh.locale.LocalizationSupport;
import com.vulinh.utils.validator.ApplicationError;

/**
 * Standard response envelope used to wrap every payload returned from the API. It carries a
 * machine-readable {@link #errorCode}, a human-readable, already-localized {@link #displayMessage},
 * and the optional {@link #data} payload.
 *
 * <p>Null fields are stripped from JSON output via {@link Include#NON_NULL}, so a successful empty
 * response stays compact and an error response omits the missing payload.
 *
 * <p>Use the static factories ({@link #success(Object)}, {@link #toError(ApplicationException)},
 * {@link #internalServerError(Object...)}) for the common cases, the {@link Builder} for ad-hoc
 * construction, or the {@code with*} methods for incremental copies of an existing instance.
 *
 * @param errorCode the service code, typically resolved from an {@link ApplicationError}
 * @param displayMessage the localized, user-facing message
 * @param data the response payload, or {@code null} when there is nothing to return
 * @param <T> the type of the response payload
 */
@JsonInclude(Include.NON_NULL)
public record GenericResponse<T>(String errorCode, String displayMessage, T data) {

  /**
   * Build a successful response wrapping the given payload. The error code and display message are
   * taken from {@link CommonServiceCodeError#MESSAGE_SUCCESS} and the message is localized through
   * {@link LocalizationSupport}.
   *
   * @param data the payload to wrap; may be {@code null}
   * @param <T> the type of the payload
   * @return a success-coded {@link GenericResponse}
   */
  public static <T> GenericResponse<T> success(T data) {
    return GenericResponse.<T>builder()
        .errorCode(CommonServiceCodeError.MESSAGE_SUCCESS.getErrorCode())
        .displayMessage(
            LocalizationSupport.getParsedMessage(CommonServiceCodeError.MESSAGE_SUCCESS))
        .data(data)
        .build();
  }

  /**
   * Build an error response from an {@link ApplicationException}. The error code is taken from the
   * exception's {@link ApplicationError} and the display message is localized using the exception's
   * arguments.
   *
   * @param applicationException the exception to translate
   * @param <T> the (typically unused) payload type
   * @return an error-coded {@link GenericResponse} with no payload
   */
  public static <T> GenericResponse<T> toError(ApplicationException applicationException) {
    return GenericResponse.<T>builder(applicationException).build();
  }

  /**
   * Return a copy of this response with a different error code.
   *
   * @param errorCode the replacement error code
   * @return a new {@link GenericResponse} with all other fields preserved
   */
  public GenericResponse<T> withErrorCode(String errorCode) {
    return new GenericResponse<>(errorCode, displayMessage, data);
  }

  /**
   * Return a copy of this response with a different error code, sourced from an {@link
   * ApplicationError}.
   *
   * @param error the application error whose code to use
   * @return a new {@link GenericResponse} with all other fields preserved
   */
  public GenericResponse<T> withErrorCode(ApplicationError error) {
    return withErrorCode(error.getErrorCode());
  }

  /**
   * Return a copy of this response with a different display message.
   *
   * @param displayMessage the replacement, already-localized message
   * @return a new {@link GenericResponse} with all other fields preserved
   */
  public GenericResponse<T> withDisplayMessage(String displayMessage) {
    return new GenericResponse<>(errorCode, displayMessage, data);
  }

  /**
   * Return a copy of this response with a different payload.
   *
   * @param data the replacement payload
   * @return a new {@link GenericResponse} with all other fields preserved
   */
  public GenericResponse<T> withData(T data) {
    return new GenericResponse<>(errorCode, displayMessage, data);
  }

  /**
   * Start a fresh {@link Builder}.
   *
   * @param <T> the payload type the future response will carry
   * @return an empty builder
   */
  public static <T> GenericResponse.Builder<T> builder() {
    return new GenericResponse.Builder<>();
  }

  /**
   * Start a {@link Builder} pre-populated with the error code and localized display message of the
   * given {@link ApplicationException}. The payload stays unset; call {@link Builder#data(Object)}
   * if you need to attach one.
   *
   * @param applicationException the exception whose error info pre-fills the builder
   * @param <T> the payload type the future response will carry
   * @return a builder seeded from the exception
   */
  public static <T> GenericResponse.Builder<T> builder(ApplicationException applicationException) {
    var applicationError = applicationException.getApplicationError();

    return new Builder<T>()
        .errorCode(applicationError)
        .displayMessage(
            LocalizationSupport.getParsedMessage(applicationError, applicationException.getArgs()));
  }

  /**
   * Build a generic internal-server-error response. The error code and message come from {@link
   * CommonServiceCodeError#MESSAGE_INTERNAL_ERROR} with the supplied arguments interpolated into
   * the localized message.
   *
   * @param args arguments substituted into the localized message template
   * @return an internal-server-error {@link GenericResponse}
   */
  public static GenericResponse<Object> internalServerError(Object... args) {
    return new Builder<>()
        .errorCode(CommonServiceCodeError.MESSAGE_INTERNAL_ERROR)
        .displayMessage(
            LocalizationSupport.getParsedMessage(
                CommonServiceCodeError.MESSAGE_INTERNAL_ERROR, args))
        .build();
  }

  /**
   * Return a new {@link Builder} initialized with the current instance's values.
   *
   * @return a new {@link Builder} with current values
   */
  public GenericResponse.Builder<T> toBuilder() {
    return new GenericResponse.Builder<T>()
        .errorCode(errorCode)
        .displayMessage(displayMessage)
        .data(data);
  }

  /**
   * Mutable, fluent builder for {@link GenericResponse}. Useful when neither {@link
   * GenericResponse#success(Object)} nor {@link GenericResponse#toError(ApplicationException)}
   * fits, or when fields need to be set incrementally.
   *
   * @param <T> the payload type
   */
  public static class Builder<T> {

    private String errorCode;
    private String displayMessage;
    private T data;

    /** Create an empty builder. All fields default to {@code null}. */
    public Builder() {
      // Default constructor does nothing
    }

    /**
     * Set the error code from an {@link ApplicationError}.
     *
     * @param error the application error whose code to use
     * @return this builder
     */
    public Builder<T> errorCode(ApplicationError error) {
      errorCode = error.getErrorCode();
      return this;
    }

    /**
     * Set the error code directly.
     *
     * @param errorCode the raw error code string
     * @return this builder
     */
    public Builder<T> errorCode(String errorCode) {
      this.errorCode = errorCode;
      return this;
    }

    /**
     * Set the localized, user-facing display message.
     *
     * @param displayMessage the message to attach
     * @return this builder
     */
    public Builder<T> displayMessage(String displayMessage) {
      this.displayMessage = displayMessage;
      return this;
    }

    /**
     * Set the response payload.
     *
     * @param data the payload
     * @return this builder
     */
    public Builder<T> data(T data) {
      this.data = data;
      return this;
    }

    /**
     * Materialize the builder into an immutable {@link GenericResponse}.
     *
     * @return the built response
     */
    public GenericResponse<T> build() {
      return new GenericResponse<>(errorCode, displayMessage, data);
    }
  }
}
