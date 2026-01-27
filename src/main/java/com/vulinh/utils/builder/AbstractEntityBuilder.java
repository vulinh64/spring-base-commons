package com.vulinh.utils.builder;

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
}
