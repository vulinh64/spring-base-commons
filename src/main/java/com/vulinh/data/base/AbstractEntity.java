package com.vulinh.data.base;

import java.io.Serial;
import java.io.Serializable;

/**
 * Base class for JPA entities; {@link Object#equals(Object)} and {@link Object#hashCode()} delegate
 * to {@link JpaIdentifiable}.
 *
 * <p>See {@link JpaIdentifiable} for the semantics of the two {@link IdType} modes and the
 * identifier-immutability contract.
 */
public abstract class AbstractEntity<I extends Serializable> implements JpaIdentifiable<I> {

  // Bruh
  @Serial private static final long serialVersionUID = 0L;

  @SuppressWarnings("EqualsDoesntCheckParameterClass")
  @Override
  public final boolean equals(Object other) {
    return JpaIdentifiable.jpaEquals(this, other);
  }

  @Override
  public final int hashCode() {
    return JpaIdentifiable.jpaHashCode(this);
  }

  // Override this if your entity use preset ID
  // (ID that is assigned manually and not by JPA providers)
  @Override
  public IdType getIdType() {
    return IdType.DYNAMIC;
  }
}
