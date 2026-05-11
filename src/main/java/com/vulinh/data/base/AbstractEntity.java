package com.vulinh.data.base;

import com.vulinh.utils.JpaEntityUtils;
import com.vulinh.utils.JpaEntityUtils.IdType;
import java.io.Serial;
import java.io.Serializable;

/**
 * Base class for JPA entities; {@link Object#equals(Object)} and {@link Object#hashCode()} delegate
 * to {@link JpaEntityUtils}.
 *
 * <p>See {@link JpaEntityUtils} for the semantics of the two {@link IdType} modes and the
 * identifier-immutability contract.
 */
public abstract class AbstractEntity<I extends Serializable>
    implements Identifiable<I>, Serializable {

  // Bruh
  @Serial private static final long serialVersionUID = 0L;

  @SuppressWarnings("EqualsDoesntCheckParameterClass")
  @Override
  public final boolean equals(Object other) {
    return JpaEntityUtils.jpaEquals(this, other);
  }

  @Override
  public final int hashCode() {
    return JpaEntityUtils.jpaHashCode(this);
  }

  public IdType getIdType() {
    return IdType.DYNAMIC;
  }
}
