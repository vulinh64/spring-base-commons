package com.vulinh.springcron;

import static org.junit.jupiter.api.Assertions.*;

import com.vulinh.springcron.data.*;
import com.vulinh.springcron.input.*;
import org.junit.jupiter.api.Test;

class SpringCronInputTest {

  @Test
  void defaultConstantProducesEveryForAllParts() {
    assertEquals("* * * * * *", SpringCronInput.DEFAULT.toExpression());
  }

  @Test
  void testSpecificCronExpression() {
    var result =
        SpringCronInput.builder()
            .secondInput(
                SecondMinuteCronInput.builder()
                    .expression(SecondMinuteExpression.EVERY_N)
                    .arguments(2)
                    .build())
            .minuteInput(
                SecondMinuteCronInput.builder()
                    .expression(SecondMinuteExpression.BETWEEN)
                    .arguments(10, 20)
                    .build())
            .hourInput(
                HourCronInput.builder()
                    .expression(HourExpression.SPECIFIC_HOURS)
                    .arguments(1, 2, 3, 4, 5, 6, 7, 8)
                    .build())
            .dayInput(
                DayCronInput.builder().expression(DayExpression.N_TO_LAST_DAY).arguments(5).build())
            .monthInput(
                MonthCronInput.builder()
                    .expression(MonthExpression.SPECIFIC_MONTHS)
                    .arguments(1, 3, 5, 7, 9, 11)
                    .build())
            .weekDayInput(
                WeekDayCronInput.builder()
                    .expression(WeekDayExpression.LAST_OF_MONTH)
                    .arguments(0)
                    .build())
            .build();

    // The expression does not need to make sense logically, we are just testing the generation here
    assertEquals(
        "*/2 10-20 1,2,3,4,5,6,7,8 L-5 JAN,MAR,MAY,JUL,SEP,NOV SUNL", result.toExpression());
  }
}
