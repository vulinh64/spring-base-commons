package com.vulinh.utils.springcron.input;

import com.vulinh.utils.springcron.CronInput;
import com.vulinh.utils.springcron.CronInputBuilder;
import com.vulinh.utils.springcron.data.DayExpression;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

/** Record representing the input for the day component of a cron expression. */
public record DayCronInput(DayExpression expression, int[] arguments)
    implements CronInput<DayExpression> {

  /** Default DayCronInput instance with the expression set to EVERY_DAY and no arguments. */
  public static final DayCronInput DEFAULT =
      new DayCronInput(DayExpression.EVERY_DAY, ArrayUtils.EMPTY_INT_ARRAY);

  public DayCronInput {
    expression = ObjectUtils.getIfNull(expression, DayExpression.EVERY_DAY);
    arguments = ArrayUtils.clone(ArrayUtils.nullToEmpty(arguments));
  }

  public DayCronInput withExpression(DayExpression expression) {
    return this.expression == expression ? this : new DayCronInput(expression, arguments);
  }

  public DayCronInput withArguments(int... arguments) {
    return Arrays.equals(this.arguments, arguments)
        ? this
        : new DayCronInput(expression, arguments);
  }

  public static DayCronInputBuilder builder() {
    return new DayCronInputBuilder();
  }

  public static class DayCronInputBuilder implements CronInputBuilder<DayExpression, DayCronInput> {

    private DayExpression expression;
    private int[] arguments;

    DayCronInputBuilder() {}

    @Override
    public DayCronInputBuilder expression(DayExpression expression) {
      this.expression = expression;
      return this;
    }

    @Override
    public DayCronInputBuilder arguments(int... arguments) {
      this.arguments = arguments;
      return this;
    }

    @Override
    public DayCronInput build() {
      return new DayCronInput(expression, arguments);
    }
  }
}
