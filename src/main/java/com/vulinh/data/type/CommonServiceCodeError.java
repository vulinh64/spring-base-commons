package com.vulinh.data.type;

import com.vulinh.utils.validator.ApplicationError;

public enum CommonServiceCodeError implements ApplicationError {
  MESSAGE_SUCCESS("app.success"),
  MESSAGE_INTERNAL_ERROR("app.internal-server-error");

  private final String errorCode;

  CommonServiceCodeError(String errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public String getErrorCode() {
    return errorCode;
  }
}
