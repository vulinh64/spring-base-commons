package com.vulinh.springcron.input;

import com.vulinh.springcron.CronInput;
import com.vulinh.springcron.CronInputBuilder;
import com.vulinh.springcron.data.MonthExpression;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

/** Record representing the input for the month component of a cron expression. */
public record MonthCronInput(MonthExpression expression, int[] arguments)
    implements CronInput<MonthExpression> {

  /** Default MonthCronInput instance with the expression set to EVERY_MONTH and no arguments. */
  public static final MonthCronInput DEFAULT =
      new MonthCronInput(MonthExpression.EVERY_MONTH, ArrayUtils.EMPTY_INT_ARRAY);

  public MonthCronInput {
    expression = ObjectUtils.getIfNull(expression, MonthExpression.EVERY_MONTH);
    arguments = ArrayUtils.clone(ArrayUtils.nullToEmpty(arguments));
  }

  public MonthCronInput withExpression(MonthExpression expression) {
    return this.expression == expression ? this : new MonthCronInput(expression, arguments);
  }

  public MonthCronInput withArguments(int... arguments) {
    return Arrays.equals(this.arguments, arguments)
        ? this
        : new MonthCronInput(expression, arguments);
  }

  public static MonthCronInputBuilder builder() {
    return new MonthCronInputBuilder();
  }

  public static class MonthCronInputBuilder
      implements CronInputBuilder<MonthExpression, MonthCronInput> {

    private MonthExpression expression;
    private int[] arguments;

    MonthCronInputBuilder() {}

    @Override
    public MonthCronInputBuilder expression(MonthExpression expression) {
      this.expression = expression;
      return this;
    }

    @Override
    public MonthCronInputBuilder arguments(int... arguments) {
      this.arguments = arguments;
      return this;
    }

    @Override
    public MonthCronInput build() {
      return new MonthCronInput(expression, arguments);
    }
  }
}
