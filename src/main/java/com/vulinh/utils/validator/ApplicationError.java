package com.vulinh.utils.validator;

import org.springframework.lang.NonNull;

/** Represents an application error with a specific error code. */
@FunctionalInterface
public interface ApplicationError {

  /**
   * Gets the error code associated with this application error. It often should be mapped with i18n
   * data.
   *
   * @return the non-null error code string
   */
  @NonNull
  String getErrorCode();
}
