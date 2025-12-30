package com.vulinh.utils.intersectedrange;

import java.io.Serial;

public class IntersectedRangeException extends RuntimeException {

  @Serial private static final long serialVersionUID = -5944062431796651971L;

  public IntersectedRangeException(String message) {
    super(message);
  }
}
