package com.vulinh.exception;

import com.vulinh.data.type.CommonServiceCodeError;
import java.io.Serial;

/**
 * Thrown when an entity declared as {@link com.vulinh.data.base.AbstractEntity.IdType#CONCRETE}
 * exposes a {@code null} identifier through {@link com.vulinh.data.base.AbstractEntity#equals} or
 * {@link com.vulinh.data.base.AbstractEntity#hashCode}.
 */
public class ConcreteEntityIdMissingException extends ApplicationException {

  @Serial private static final long serialVersionUID = 0L;

  public ConcreteEntityIdMissingException(String message, Object... args) {
    super(message, CommonServiceCodeError.MESSAGE_INVALID_CONCRETE_ID, args);
  }

  public ConcreteEntityIdMissingException(String message, Throwable cause, Object... args) {
    super(message, CommonServiceCodeError.MESSAGE_INVALID_CONCRETE_ID, cause, args);
  }
}
