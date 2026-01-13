package com.vulinh.utils;

/** Shared common utility methods. */
public class CommonUtils {

  private CommonUtils() {}

  /**
   * Get first three letters of an enum value.
   *
   * @param value enum value
   * @return first three letters of the enum value.
   */
  public static String first3Letters(Enum<?> value) {
    return value.name().substring(0, 3);
  }
}
