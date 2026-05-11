package com.vulinh.exception;

import com.vulinh.data.dto.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Base class for service-side exception handlers, providing a shared catch-all fallback for any
 * uncaught {@link RuntimeException}, as well as other common handling strategies across all
 * services.
 *
 * <h2>Usage</h2>
 *
 * <p>This class is intentionally <em>not</em> annotated with {@link
 * org.springframework.web.bind.annotation.ControllerAdvice} or {@link
 * org.springframework.web.bind.annotation.RestControllerAdvice}, so it does nothing on its own: it
 * is dormant unless a consuming service explicitly extends it. The expected usage is:
 *
 * <pre>{@code
 * @RestControllerAdvice
 * public class MyServiceExceptionHandler extends CommonExceptionHandler {
 *
 *   @ExceptionHandler(MyValidationException.class)
 *   public ResponseEntity<GenericResponse<Object>> handleValidation(MyValidationException ex) {
 *     return ResponseEntity.badRequest().body(GenericResponse.toError(ex));
 *   }
 *
 *   // ...other service-specific handlers
 * }
 * }</pre>
 *
 * <h2>Fallback behavior</h2>
 *
 * <p>Spring's exception-resolution picks the most specific {@link ExceptionHandler} match, so
 * service-declared handlers always win over the inherited fallback. The fallback only fires when:
 *
 * <ul>
 *   <li>An {@link ApplicationException} subclass is thrown that the service forgot to register an
 *       explicit handler for, in which case the response carries the exception's localized error
 *       code and message but a {@code 500} status, signaling a missing handler registration.
 *   <li>An unexpected {@link RuntimeException} escapes, that will be answered with a generic {@code
 *       500} body built from {@link GenericResponse#internalServerError(Object...)}.
 * </ul>
 *
 * <h2>Customization</h2>
 *
 * <p>Subclasses may also override {@link #handleRuntimeException(RuntimeException)} to customize
 * fallback behavior shared across services (for example, additional logging or response shaping).
 *
 * <h2>Return type</h2>
 *
 * <p>Handler methods deliberately return {@link ResponseEntity} rather than a bare {@link
 * GenericResponse}, so subclasses can be annotated with either {@link
 * org.springframework.web.bind.annotation.ControllerAdvice} or {@link
 * org.springframework.web.bind.annotation.RestControllerAdvice}.
 *
 * <h2>Built-in error codes</h2>
 *
 * <p>The library ships some reserved error codes in {@link
 * com.vulinh.data.type.CommonServiceCodeError} that this handler (and {@link GenericResponse})
 * relies on:
 *
 * <ul>
 *   <li>{@code app.success} ({@link com.vulinh.data.type.CommonServiceCodeError#MESSAGE_SUCCESS}):
 *       {@code MESSAGE_SUCCESS}, used by {@link GenericResponse#success(Object)}.
 *   <li>{@code app.internal-server-error} ({@link
 *       com.vulinh.data.type.CommonServiceCodeError#MESSAGE_INTERNAL_ERROR}): {@code
 *       MESSAGE_INTERNAL_ERROR}, used by the fallback above via {@link
 *       GenericResponse#internalServerError(Object...)}.
 *   <li>{@code app.entity.concrete-id-missing} ({@link
 *       com.vulinh.data.type.CommonServiceCodeError#MESSAGE_INVALID_CONCRETE_ID}): {@code
 *       MESSAGE_INVALID_CONCRETE_ID}, carried by {@link ConcreteEntityIdMissingException} when a
 *       {@link com.vulinh.utils.JpaEntityUtils.IdType#CONCRETE} entity is observed with a {@code
 *       null} identifier through {@code equals} / {@code hashCode}.
 * </ul>
 *
 * <p>Consuming services should provide localized values for these keys in their own resource
 * bundles (see {@link com.vulinh.locale.LocalizationBundleProvider}); otherwise responses fall back
 * to the raw codes.
 */
public abstract class CommonExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CommonExceptionHandler.class);

  /**
   * Fallback handler for any {@link RuntimeException} that no more specific {@link
   * ExceptionHandler} method has claimed.
   *
   * <p>Always responds with HTTP {@code 500}. {@link ApplicationException} subclasses are expected
   * to be handled explicitly by the consuming service with the appropriate status (4xx / 5xx); they
   * only reach this method when such a handler is missing.
   */
  @ExceptionHandler(RuntimeException.class)
  protected ResponseEntity<GenericResponse<Object>> handleRuntimeException(
      RuntimeException runtimeException) {
    LOGGER.error(runtimeException.getMessage(), runtimeException);

    return ResponseEntity.internalServerError()
        .body(
            runtimeException instanceof ApplicationException applicationException
                ? GenericResponse.toError(applicationException)
                : GenericResponse.internalServerError());
  }

  /**
   * Handler for {@link ApplicationValidationException}, the canonical signal that an inbound
   * request failed validation.
   *
   * <p>Always responds with HTTP {@code 400}, carrying the exception's localized error code and
   * message via {@link GenericResponse#toError(ApplicationException)}. The exception message is
   * logged at {@code DEBUG} level: validation failures are expected client errors, not defects, so
   * they do not warrant a stack trace.
   */
  @ExceptionHandler(ApplicationValidationException.class)
  public ResponseEntity<GenericResponse<Object>> handleApplicationValidationException(
      ApplicationValidationException ex) {
    LOGGER.debug(ex.getMessage());

    return ResponseEntity.badRequest().body(GenericResponse.toError(ex));
  }
}
