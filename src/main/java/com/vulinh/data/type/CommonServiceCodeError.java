package com.vulinh.data.type;

import com.vulinh.utils.validator.ApplicationError;

public enum CommonServiceCodeError implements ApplicationError {
  MESSAGE_SUCCESS("app.success"),
  MESSAGE_INTERNAL_ERROR("app.internal-server-error"),
  MESSAGE_INVALID_CONCRETE_ID("app.entity.concrete-id-missing");

  private final String errorCode;

  CommonServiceCodeError(String errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public String getErrorCode() {
    return errorCode;
  }
}
