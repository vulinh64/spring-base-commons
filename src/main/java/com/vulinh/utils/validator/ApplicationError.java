package com.vulinh.utils.validator;

/** Represents an application error with a specific error code. */
@FunctionalInterface
public interface ApplicationError {

  /**
   * Gets the error code associated with this application error. It often should be mapped with i18n
   * data.
   *
   * @return the non-null error code string
   */
  String getErrorCode();
}
