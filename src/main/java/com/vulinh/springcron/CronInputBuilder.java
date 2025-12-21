package com.vulinh.springcron;

/**
 * Base interface for cron input builder.
 *
 * @param <P> the cron part expression type
 * @param <C> the cron input type
 */
public interface CronInputBuilder<P extends PartExpression, C extends CronInput<? super P>> {

  /**
   * Initialize the expression.
   *
   * @param expression the provided expression.
   * @return the builder object.
   */
  CronInputBuilder<P, C> expression(P expression);

  /**
   * Initialize the arguments.
   *
   * @param arguments the provided arguments.
   * @return the builder object.
   */
  CronInputBuilder<P, C> arguments(int... arguments);

  /**
   * Return the cron input object.
   *
   * @return the cron input object.
   */
  C build();
}
