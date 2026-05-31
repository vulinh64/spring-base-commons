package com.vulinh.utils.jpaentitybuilder;

import com.vulinh.data.base.AbstractEntity;
import java.io.Serializable;

/**
 * Base entity builder interface.
 *
 * @param <I> Entity's ID type
 * @param <E> Entity type
 * @param <B> Builder type (self type)
 */
public interface AbstractEntityBuilder<
    I extends Serializable, E extends AbstractEntity<I>, B extends AbstractEntityBuilder<I, E, B>> {

  /**
   * Return the builder itself.
   *
   * @return the builder itself
   */
  B self();

  /**
   * Finalizing the entity building process.
   *
   * @return the concreted entity
   */
  E build();

  /**
   * Return a new builder initialized with this builder's current values.
   *
   * <p>Implementations should use this when the caller needs to branch from an existing builder
   * without mutating it.
   *
   * @return a new builder initialized from this builder
   */
  B toBuilder();
}
