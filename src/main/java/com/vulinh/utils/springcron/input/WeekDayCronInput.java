package com.vulinh.utils.springcron.input;

import com.vulinh.utils.springcron.CronInput;
import com.vulinh.utils.springcron.CronInputBuilder;
import com.vulinh.utils.springcron.data.WeekDayExpression;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

/** Record representing the input for the weekday component of a cron expression. */
public record WeekDayCronInput(WeekDayExpression expression, int[] arguments)
    implements CronInput<WeekDayExpression> {

  /**
   * Default WeekDayCronInput instance with the expression set to EVERY_WEEK_DAY and no arguments.
   */
  public static final WeekDayCronInput DEFAULT =
      new WeekDayCronInput(null, ArrayUtils.EMPTY_INT_ARRAY);

  public WeekDayCronInput {
    expression = ObjectUtils.getIfNull(expression, WeekDayExpression.EVERY_WEEK_DAY);
    arguments = ArrayUtils.clone(ArrayUtils.nullToEmpty(arguments));
  }

  public static WeekDayCronInputBuilder builder() {
    return new WeekDayCronInputBuilder();
  }

  public WeekDayCronInput withExpression(WeekDayExpression expression) {
    return this.expression == expression ? this : new WeekDayCronInput(expression, arguments);
  }

  public WeekDayCronInput withArguments(int... arguments) {
    return Arrays.equals(this.arguments, arguments)
        ? this
        : new WeekDayCronInput(expression, arguments);
  }

  public static class WeekDayCronInputBuilder
      implements CronInputBuilder<WeekDayExpression, WeekDayCronInput> {

    private WeekDayExpression expression;
    private int[] arguments;

    WeekDayCronInputBuilder() {}

    @Override
    public WeekDayCronInputBuilder expression(WeekDayExpression expression) {
      this.expression = expression;
      return this;
    }

    @Override
    public WeekDayCronInputBuilder arguments(int... arguments) {
      this.arguments = arguments;
      return this;
    }

    @Override
    public WeekDayCronInput build() {
      return new WeekDayCronInput(expression, arguments);
    }
  }
}
