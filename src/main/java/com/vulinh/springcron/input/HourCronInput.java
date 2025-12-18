package com.vulinh.springcron.input;

import com.vulinh.springcron.CronInput;
import com.vulinh.springcron.CronInputBuilder;
import com.vulinh.springcron.data.HourExpression;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

/** Record representing the input for the hour component of a cron expression. */
public record HourCronInput(HourExpression expression, int[] arguments)
    implements CronInput<HourExpression> {

  /** Default HourCronInput instance with the expression set to EVERY_HOUR and no arguments. */
  public static final HourCronInput DEFAULT =
      new HourCronInput(HourExpression.EVERY_HOUR, ArrayUtils.EMPTY_INT_ARRAY);

  public HourCronInput {
    expression = ObjectUtils.getIfNull(expression, HourExpression.EVERY_HOUR);
    arguments = ArrayUtils.clone(ArrayUtils.nullToEmpty(arguments));
  }

  public HourCronInput withExpression(HourExpression expression) {
    return this.expression == expression ? this : new HourCronInput(expression, arguments);
  }

  public HourCronInput withArguments(int... arguments) {
    return Arrays.equals(this.arguments, arguments)
        ? this
        : new HourCronInput(expression, arguments);
  }

  public static HourCronInputBuilder builder() {
    return new HourCronInputBuilder();
  }

  public static class HourCronInputBuilder
      implements CronInputBuilder<HourExpression, HourCronInput> {

    private HourExpression expression;
    private int[] arguments;

    HourCronInputBuilder() {}

    @Override
    public HourCronInputBuilder expression(HourExpression expression) {
      this.expression = expression;
      return this;
    }

    @Override
    public HourCronInputBuilder arguments(int... arguments) {
      this.arguments = arguments;
      return this;
    }

    @Override
    public HourCronInput build() {
      return new HourCronInput(expression, arguments);
    }
  }
}
