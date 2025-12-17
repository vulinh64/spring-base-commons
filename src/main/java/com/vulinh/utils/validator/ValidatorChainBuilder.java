package com.vulinh.utils.validator;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.springframework.lang.Nullable;

/**
 * Builder for {@link ValidatorChain}
 *
 * @param <T> the type of object to be validated
 */
public class ValidatorChainBuilder<T> {

  /** The list of validator steps in the chain. */
  // LinkedList will ensure that adding operation is cheap.
  private final List<ValidatorStep<T>> validatorSteps;

  /** Constructs a ValidatorChainBuilder. */
  public ValidatorChainBuilder() {
    validatorSteps = new LinkedList<>();
  }

  /**
   * Adds a validator step to the chain.
   *
   * @param step the validator step to add. Can be null.
   * @return the current ValidatorChainBuilder instance
   */
  public final ValidatorChainBuilder<T> add(@Nullable ValidatorStep<T> step) {
    if (step != null) {
      validatorSteps.add(step);
    }

    return this;
  }

  /**
   * Adds multiple validator steps to the chain.
   *
   * @param steps the validator step array to add. Can be null.
   * @return the current ValidatorChainBuilder instance
   */
  @SafeVarargs
  public final ValidatorChainBuilder<T> add(@Nullable ValidatorStep<T>... steps) {
    return add(steps == null ? List.of() : Arrays.asList(steps));
  }

  /**
   * Adds multiple validator steps to the chain.
   *
   * @param chains the validator step collection to add. Can be null.
   * @return the current ValidatorChainBuilder instance
   */
  public final ValidatorChainBuilder<T> add(Collection<? extends ValidatorStep<T>> chains) {
    if (chains == null || chains.isEmpty()) {
      return this;
    }

    for (var chain : chains) {
      if (chain != null) {
        validatorSteps.add(chain);
      }
    }

    return this;
  }

  /**
   * Builds the ValidatorChain.
   *
   * @return the constructed ValidatorChain
   */
  public ValidatorChain<T> build() {
    return new ValidatorChain<>(validatorSteps);
  }
}
