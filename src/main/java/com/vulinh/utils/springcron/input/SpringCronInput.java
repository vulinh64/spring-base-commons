package com.vulinh.utils.springcron.input;

import com.vulinh.utils.springcron.CronInput;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;

/**
 * Record representing the input for a Spring cron expression, including second, minute, hour, day,
 * month, and weekday components.
 */
public record SpringCronInput(
    SecondMinuteCronInput secondInput,
    SecondMinuteCronInput minuteInput,
    HourCronInput hourInput,
    DayCronInput dayInput,
    MonthCronInput monthInput,
    WeekDayCronInput weekDayInput) {

  /** Default SpringCronInput instance with all components set to their default values. */
  public static final SpringCronInput DEFAULT =
      new SpringCronInput(
          SecondMinuteCronInput.DEFAULT,
          SecondMinuteCronInput.DEFAULT,
          HourCronInput.DEFAULT,
          DayCronInput.DEFAULT,
          MonthCronInput.DEFAULT,
          WeekDayCronInput.DEFAULT);

  public SpringCronInput {
    secondInput = ObjectUtils.getIfNull(secondInput, SecondMinuteCronInput.DEFAULT);
    minuteInput = ObjectUtils.getIfNull(minuteInput, SecondMinuteCronInput.DEFAULT);
    hourInput = ObjectUtils.getIfNull(hourInput, HourCronInput.DEFAULT);
    dayInput = ObjectUtils.getIfNull(dayInput, DayCronInput.DEFAULT);
    monthInput = ObjectUtils.getIfNull(monthInput, MonthCronInput.DEFAULT);
    weekDayInput = ObjectUtils.getIfNull(weekDayInput, WeekDayCronInput.DEFAULT);
  }

  public String toExpression() {
    return Stream.<CronInput<?>>of(
            secondInput, minuteInput, hourInput, dayInput, monthInput, weekDayInput)
        .map(CronInput::toPartExpression)
        .collect(Collectors.joining(" "));
  }

  public static SpringCronInputBuilder builder() {
    return new SpringCronInputBuilder();
  }

  @Override
  @NonNull
  public String toString() {
    return toExpression();
  }

  public SpringCronInput withSecondInput(SecondMinuteCronInput secondInput) {
    return this.secondInput.strictlyEquals(secondInput)
        ? this
        : new SpringCronInput(
            secondInput, minuteInput, hourInput, dayInput, monthInput, weekDayInput);
  }

  public SpringCronInput withMinuteInput(SecondMinuteCronInput minuteInput) {
    return this.minuteInput.strictlyEquals(minuteInput)
        ? this
        : new SpringCronInput(
            secondInput, minuteInput, hourInput, dayInput, monthInput, weekDayInput);
  }

  public SpringCronInput withHourInput(HourCronInput hourInput) {
    return this.hourInput.strictlyEquals(hourInput)
        ? this
        : new SpringCronInput(
            secondInput, minuteInput, hourInput, dayInput, monthInput, weekDayInput);
  }

  public SpringCronInput withDayInput(DayCronInput dayInput) {
    return this.dayInput.strictlyEquals(dayInput)
        ? this
        : new SpringCronInput(
            secondInput, minuteInput, hourInput, dayInput, monthInput, weekDayInput);
  }

  public SpringCronInput withMonthInput(MonthCronInput monthInput) {
    return this.monthInput.strictlyEquals(monthInput)
        ? this
        : new SpringCronInput(
            secondInput, minuteInput, hourInput, dayInput, monthInput, weekDayInput);
  }

  public SpringCronInput withWeekDayInput(WeekDayCronInput weekDayInput) {
    return this.weekDayInput.strictlyEquals(weekDayInput)
        ? this
        : new SpringCronInput(
            secondInput, minuteInput, hourInput, dayInput, monthInput, weekDayInput);
  }

  public static class SpringCronInputBuilder {

    private SecondMinuteCronInput secondInput;
    private SecondMinuteCronInput minuteInput;
    private HourCronInput hourInput;
    private DayCronInput dayInput;
    private MonthCronInput monthInput;
    private WeekDayCronInput weekDayInput;

    SpringCronInputBuilder() {}

    public SpringCronInputBuilder secondInput(SecondMinuteCronInput secondInput) {
      this.secondInput = secondInput;
      return this;
    }

    public SpringCronInputBuilder minuteInput(SecondMinuteCronInput minuteInput) {
      this.minuteInput = minuteInput;
      return this;
    }

    public SpringCronInputBuilder hourInput(HourCronInput hourInput) {
      this.hourInput = hourInput;
      return this;
    }

    public SpringCronInputBuilder dayInput(DayCronInput dayInput) {
      this.dayInput = dayInput;
      return this;
    }

    public SpringCronInputBuilder monthInput(MonthCronInput monthInput) {
      this.monthInput = monthInput;
      return this;
    }

    public SpringCronInputBuilder weekDayInput(WeekDayCronInput weekDayInput) {
      this.weekDayInput = weekDayInput;
      return this;
    }

    public SpringCronInput build() {
      return new SpringCronInput(
          secondInput, minuteInput, hourInput, dayInput, monthInput, weekDayInput);
    }
  }
}
