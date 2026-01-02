package com.vulinh.utils.springcron;

import com.vulinh.exception.SpringCronException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/** Interface representing a part of a cron expression. */
public interface PartExpression {

  /**
   * The predicate used to validate the arguments.
   *
   * @return The predicate for argument validation.
   */
  Predicate<List<Integer>> getPredicate();

  /**
   * The function used to generate the cron expression string.
   *
   * @return The function for generating the cron expression.
   */
  Function<List<Integer>, String> getGenerator();

  /**
   * Converts the provided arguments into a cron expression string after validation.
   *
   * @param arguments The list of arguments to convert.
   * @return The cron expression as a string.
   * @throws SpringCronException If the arguments are invalid.
   */
  default String generateFinalExpression(int... arguments) {
    var args = IntStream.of(arguments).boxed().toList();

    if (!getPredicate().test(args)) {
      throw new SpringCronException("Invalid arguments: %s".formatted(args));
    }

    return getGenerator().apply(args);
  }
}
