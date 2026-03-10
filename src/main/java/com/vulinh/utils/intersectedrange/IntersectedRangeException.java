package com.vulinh.utils.intersectedrange;

import java.io.Serial;

public class IntersectedRangeException extends RuntimeException {

  // SERIOUSLY, STOP
  @Serial private static final long serialVersionUID = 0L;

  public IntersectedRangeException(String message) {
    super(message);
  }
}
