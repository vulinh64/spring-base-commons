package com.vulinh.data.base;

import com.vulinh.exception.ConcreteEntityIdMissingException;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;

/**
 * JPA-flavored {@link Identifiable} that augments the contract with the notion of how the
 * identifier is assigned ({@link IdType#DYNAMIC} vs {@link IdType#CONCRETE}), and exposes the
 * canonical {@link Object#equals(Object)} and {@link Object#hashCode()} implementations used by
 * {@link AbstractEntity} as the static helpers {@link #jpaEquals(JpaIdentifiable, Object)} and
 * {@link #jpaHashCode(JpaIdentifiable)}.
 *
 * <p>Entities that want this implementation but cannot extend {@link AbstractEntity}, typically
 * because doing so would push their inheritance depth past the limit enforced by SonarQube and
 * block the build, can call these helpers directly from their own {@code equals} / {@code hashCode}
 * overrides.
 *
 * <p>See this <a
 * href="https://jpa-buddy.com/blog/hopefully-the-final-article-about-equals-and-hashcode-for-jpa-entities-with-db-generated-ids/">Guide
 * on How to Implement JPA Entities equals() and hashCode()</a> for more information.
 *
 * <h2>ID generation modes</h2>
 *
 * <p>The semantics of {@link #jpaEquals(JpaIdentifiable, Object)} and {@link
 * #jpaHashCode(JpaIdentifiable)} adapt to how the entity's identifier is assigned, declared by
 * {@link #getIdType()}:
 *
 * <ul>
 *   <li><strong>{@link IdType#DYNAMIC}</strong> (default) — the identifier is assigned by the
 *       persistence provider (database sequence, generated UUID, and the like). A transient
 *       instance has a {@code null} id and is intentionally never equal to any other instance.
 *       {@code hashCode()} is always derived from the effective class (never from the id) so that
 *       the hash stays stable across the transient → persisted transition; this keeps the entity
 *       usable in hash-based collections before the provider assigns an id.
 *   <li><strong>{@link IdType#CONCRETE}</strong> — the identifier is assigned by application code
 *       before the entity is persisted. Two transient instances sharing the same preset id are
 *       equal, and {@code hashCode()} is computed from the id directly. A {@code null} id under
 *       this mode is a contract violation and triggers {@link ConcreteEntityIdMissingException}.
 * </ul>
 *
 * <h2>Identifier immutability</h2>
 *
 * <p>This is not a JPA-specific concern: any Java object whose {@code equals} / {@code hashCode}
 * depends on mutable state will behave incorrectly once used as a key in {@link java.util.HashMap},
 * {@link java.util.HashSet}, or any other hash-based structure. The ID is simply the canonical
 * identity for entities, so the rule surfaces most visibly here.
 *
 * <p>Concretely, regardless of the chosen mode, the identifier returned by {@link #getId()} must be
 * <em>effectively immutable</em> for the lifetime of the instance. Both {@link
 * #jpaEquals(JpaIdentifiable, Object)} and {@link #jpaHashCode(JpaIdentifiable)} derive from it, so
 * mutating the id after the entity has been placed in any hash-based collection (or shared with
 * caching, change-tracking, or persistence-context machinery) silently corrupts those structures.
 *
 * <p>This applies whether the key is a simple field (e.g. a {@link Long}, {@link java.util.UUID},
 * or {@link String}) or an embedded composite key (e.g. an {@link jakarta.persistence.EmbeddedId}
 * class): the underlying fields that participate in the id's {@code equals}/{@code hashCode} must
 * not change once set. Composite key classes should be designed as immutable value types.
 */
public interface JpaIdentifiable<I extends Serializable> extends Identifiable<I>, Serializable {

  enum IdType {
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

  /**
   * Returns the strategy by which this entity's identifier is assigned. Controls the semantics of
   * {@link #jpaEquals(JpaIdentifiable, Object)} and {@link #jpaHashCode(JpaIdentifiable)}. See
   * {@link JpaIdentifiable} for details.
   */
  IdType getIdType();

  /**
   * {@link Object#equals(Object)} implementation for {@link JpaIdentifiable} entities. See {@link
   * JpaIdentifiable} for the full semantics across {@link IdType#DYNAMIC} and {@link
   * IdType#CONCRETE} modes.
   */
  static boolean jpaEquals(JpaIdentifiable<?> self, Object other) {
    throwIfNullSelf(self);

    if (self == other) {
      return true;
    }

    // getEffectiveClass takes care of Hibernate proxies
    if (!(getEffectiveClass(self) == getEffectiveClass(other)
        && other instanceof JpaIdentifiable<?> ae)) {
      return false;
    }

    if (self.getIdType() == IdType.DYNAMIC) {
      var id = self.getId();

      // DYNAMIC + null id -> transient entity, never equal
      return id != null && Objects.equals(id, ae.getId());
    }

    // CONCRETE ID type can never be null
    throwIfNullConcreteId(self);

    return Objects.equals(self.getId(), ae.getId());
  }

  /**
   * {@link Object#hashCode()} implementation for {@link JpaIdentifiable} entities. See {@link
   * JpaIdentifiable} for the full semantics across {@link IdType#DYNAMIC} and {@link
   * IdType#CONCRETE} modes.
   */
  static int jpaHashCode(JpaIdentifiable<?> self) {
    throwIfNullSelf(self);

    if (self.getIdType() == IdType.DYNAMIC) {
      return getEffectiveClass(self).hashCode();
    }

    // CONCRETE ID type can never be null
    throwIfNullConcreteId(self);

    return self.getId().hashCode();
  }

  /**
   * Returns the entity's underlying persistent class, unwrapping any Hibernate lazy-loading proxy.
   * For non-proxied instances this is simply {@code object.getClass()}.
   */
  static Class<?> getEffectiveClass(Object object) {
    return object instanceof HibernateProxy proxy
        ? proxy.getHibernateLazyInitializer().getPersistentClass()
        : object.getClass();
  }

  private static void throwIfNullSelf(JpaIdentifiable<?> self) {
    if (self == null) {
      throw new IllegalArgumentException("self must not be null");
    }
  }

  private static void throwIfNullConcreteId(JpaIdentifiable<?> self) {
    if (self.getIdType() == IdType.CONCRETE && self.getId() == null) {
      throw new ConcreteEntityIdMissingException(
          "CONCRETE entity %s has null id".formatted(getEffectiveClass(self).getName()));
    }
  }
}
