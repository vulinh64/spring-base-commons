package com.vulinh.utils.springcron.input;

import com.vulinh.utils.springcron.CronInput;
import com.vulinh.utils.springcron.CronInputBuilder;
import com.vulinh.utils.springcron.data.SecondMinuteExpression;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

/** Record representing the input for the second or minute component of a cron expression. */
public record SecondMinuteCronInput(SecondMinuteExpression expression, int[] arguments)
    implements CronInput<SecondMinuteExpression> {

  /** Default SecondCronInput instance with the expression set to EVERY and no arguments. */
  public static final SecondMinuteCronInput DEFAULT =
      new SecondMinuteCronInput(SecondMinuteExpression.EVERY, ArrayUtils.EMPTY_INT_ARRAY);

  public SecondMinuteCronInput {
    expression = ObjectUtils.getIfNull(expression, SecondMinuteExpression.EVERY);
    arguments = ArrayUtils.clone(ArrayUtils.nullToEmpty(arguments));
  }

  public SecondMinuteCronInput withExpression(SecondMinuteExpression expression) {
    return this.expression == expression ? this : new SecondMinuteCronInput(expression, arguments);
  }

  public SecondMinuteCronInput withArguments(int... arguments) {
    return Arrays.equals(this.arguments, arguments)
        ? this
        : new SecondMinuteCronInput(expression, arguments);
  }

  public static SecondCronInputBuilder builder() {
    return new SecondCronInputBuilder();
  }

  public static class SecondCronInputBuilder
      implements CronInputBuilder<SecondMinuteExpression, SecondMinuteCronInput> {

    private SecondMinuteExpression expression;
    private int[] arguments;

    SecondCronInputBuilder() {}

    @Override
    public SecondCronInputBuilder expression(SecondMinuteExpression expression) {
      this.expression = expression;
      return this;
    }

    @Override
    public SecondCronInputBuilder arguments(int... arguments) {
      this.arguments = arguments;
      return this;
    }

    @Override
    public SecondMinuteCronInput build() {
      return new SecondMinuteCronInput(expression, arguments);
    }
  }
}
