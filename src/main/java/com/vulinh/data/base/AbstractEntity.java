package com.vulinh.data.base;

import com.vulinh.exception.ConcreteEntityIdMissingException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;

/**
 * A customized base abstract class for JPA entities that provides implementations for {@link
 * Object#equals(Object)} and {@link Object#hashCode()}.
 *
 * <p>See this <a
 * href="https://jpa-buddy.com/blog/hopefully-the-final-article-about-equals-and-hashcode-for-jpa-entities-with-db-generated-ids/">Guide
 * on How to Implement JPA Entities equals() and hashCode()</a> for more information.
 *
 * <h2>ID generation modes</h2>
 *
 * <p>The semantics of {@link #equals(Object)} and {@link #hashCode()} adapt to how the entity's
 * identifier is assigned, declared by overriding {@link #getIdType()}:
 *
 * <ul>
 *   <li><strong>{@link IdType#DYNAMIC}</strong> (default) — the identifier is assigned by the
 *       persistence provider (database sequence, generated UUID, and the like). A transient
 *       instance has a {@code null} id and is intentionally never equal to any other instance.
 *   <li><strong>{@link IdType#CONCRETE}</strong> — the identifier is assigned by application code
 *       before the entity is persisted. Two transient instances sharing the same preset id are
 *       equal, and {@code hashCode()} is computed from the id directly. A {@code null} id under
 *       this mode is a contract violation and triggers {@link ConcreteEntityIdMissingException}.
 * </ul>
 *
 * <h2>Identifier immutability</h2>
 *
 * <p>Regardless of the chosen mode, the identifier returned by {@link #getId()} must be
 * <em>effectively immutable</em> for the lifetime of the instance. {@link #equals(Object)} and
 * {@link #hashCode()} both derive from it, so mutating the id after the entity has been placed in
 * any hash-based collection (or shared with caching, change-tracking, or persistence-context
 * machinery) silently corrupts those structures.
 *
 * <p>This applies whether the key is a simple field (e.g. a {@link Long}, {@link java.util.UUID},
 * or {@link String}) or an embedded composite key (e.g. an {@link jakarta.persistence.EmbeddedId}
 * class): the underlying fields that participate in the id's {@code equals}/{@code hashCode} must
 * not change once set. Composite key classes should be designed as immutable value types.
 */
public abstract class AbstractEntity<I extends Serializable>
    implements Identifiable<I>, Serializable {

  public enum IdType {
    /**
     * Identifier assigned by application code before the entity is persisted. Null ids are a
     * contract violation under this mode.
     */
    CONCRETE,
    /**
     * Identifier assigned by the persistence provider. Transient instances (with a null id) are
     * never equal to any other instance. This is the default mode.
     */
    DYNAMIC
  }

  // Bruh
  @Serial private static final long serialVersionUID = 0L;

  @Override
  public final boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null) {
      return false;
    }

    var id = getId();

    if (id == null) {
      throwIfNullConcreteId();

      // DYNAMIC + null id -> transient entity, never equal
      return false;
    }

    // getEffectiveClass takes care of Hibernate proxies
    // other instanceof AbstractEntity<?> -> safe cast and check
    return getEffectiveClass(this) == getEffectiveClass(other)
        && other instanceof AbstractEntity<?> ai
        && Objects.equals(id, ai.getId());
  }

  @Override
  public final int hashCode() {
    var id = getId();

    if (id == null) {
      throwIfNullConcreteId();

      // DYNAMIC + null id -> use the proxy class' hash code so the entity stays stable in hash
      // collections before the persistence provider assigns an id
      return getEffectiveClass(this).hashCode();
    }

    return id.hashCode();
  }

  protected IdType getIdType() {
    return IdType.DYNAMIC;
  }

  private void throwIfNullConcreteId() {
    if (getIdType() == IdType.CONCRETE) {
      throw new ConcreteEntityIdMissingException(
          "CONCRETE entity %s has null id".formatted(getEffectiveClass(this).getName()));
    }
  }

  // See the link above for explanation
  static Class<?> getEffectiveClass(Object object) {
    return object instanceof HibernateProxy proxy
        ? proxy.getHibernateLazyInitializer().getPersistentClass()
        : object.getClass();
  }
}
