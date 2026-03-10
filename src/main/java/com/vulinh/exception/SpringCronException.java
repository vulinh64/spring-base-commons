package com.vulinh.exception;

import java.io.Serial;

/** Custom exception class for handling Spring Cron expression errors. */
public class SpringCronException extends IllegalArgumentException {

  // WHY DO WE EVEN NEED THIS?
  @Serial private static final long serialVersionUID = 0L;

  public SpringCronException(String message) {
    super(message);
  }
}
