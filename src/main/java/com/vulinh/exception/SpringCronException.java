package com.vulinh.exception;

import java.io.Serial;

/** Custom exception class for handling Spring Cron expression errors. */
public class SpringCronException extends IllegalArgumentException {

  @Serial private static final long serialVersionUID = -1543690173042562322L;

  public SpringCronException(String message) {
    super(message);
  }
}
