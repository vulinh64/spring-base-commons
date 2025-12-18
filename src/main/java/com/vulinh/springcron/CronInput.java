package com.vulinh.springcron;

import java.util.Arrays;
import java.util.Objects;

/** Basic representation of a single part of a Spring Cron expression. */
public interface CronInput<P extends PartExpression> {

  /**
   * The temporal part.
   *
   * @return The temporal part.
   */
  P expression();

  /**
   * Arguments
   *
   * @return Arguments provided to the expression.
   */
  int[] arguments();

  /**
   * Determine if this instance is strictly equal to another CronInput.
   *
   * @param b Second part
   * @return {@code true} if both parts are equal, {@code false} if otherwise.
   */
  default boolean strictlyEquals(CronInput<?> b) {
    return this == b
        || b != null
            && Objects.equals(expression(), b.expression())
            && Arrays.equals(arguments(), b.arguments());
  }

  /**
   * Represent the Spring Cron expression string for this Cron part.
   *
   * @return The Spring Cron expression value.
   */
  default String toPartExpression() {
    return expression().generateFinalExpression(arguments());
  }
}
