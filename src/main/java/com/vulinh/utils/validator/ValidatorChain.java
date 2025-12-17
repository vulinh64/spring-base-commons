package com.vulinh.utils.validator;

import com.vulinh.exception.ApplicationValidationException;
import java.util.Collections;
import java.util.List;

/**
 * A chain of validators to validate an object of type T.
 *
 * @param <T> the type of object to be validated
 */
public class ValidatorChain<T> {

  /** The list of validator steps in the chain. */
  private final List<ValidatorStep<T>> validatorSteps;

  /**
   * Constructs a ValidatorChain with the given list of validator steps.
   *
   * @param validatorSteps the list of validator steps
   */
  // The list of validator steps is copied to ensure immutability.
  ValidatorChain(List<? extends ValidatorStep<T>> validatorSteps) {
    this.validatorSteps =
        validatorSteps == null ? Collections.emptyList() : List.copyOf(validatorSteps);
  }

  /**
   * Validates the given object using the validator steps in the chain.
   *
   * @param object the object to be validated
   * @throws ApplicationValidationException if any validation step fails
   */
  public void validate(T object) {
    for (var step : validatorSteps) {
      if (!step.getPredicate().test(object)) {
        throw new ApplicationValidationException(
            step.getExceptionMessage(), step.getApplicationError(), step.getArgs());
      }
    }
  }
}
