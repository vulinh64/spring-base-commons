package com.vulinh.data.base;

import com.vulinh.data.Identifiable;
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
 */
public abstract class AbstractIdentifiable<I extends Serializable>
    implements Identifiable<I>, Serializable {

  // Bruh
  @Serial private static final long serialVersionUID = 0L;

  @Override
  public final boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    var id = getId();

    // id == null -> transient entity -> always not equal
    // other == null -> obviously not equal
    // getEffectiveClass takes care of Hibernate proxies
    // other instanceof AbstractIdentifiable<?> -> safe cast and check
    return id != null
        && other != null
        && getEffectiveClass(this) == getEffectiveClass(other)
        && other instanceof AbstractIdentifiable<?> ai
        && Objects.equals(id, ai.getId());
  }

  @Override
  public final int hashCode() {
    var id = getId();

    // Either use the proxy class' hash code or the id's hash code
    // For concrete JPA entities (not have any many-to-one connections to the others)
    return id == null ? getEffectiveClass(this).hashCode() : id.hashCode();
  }

  // See the link above for explanation
  private static Class<?> getEffectiveClass(Object object) {
    return object instanceof HibernateProxy proxy
        ? proxy.getHibernateLazyInitializer().getPersistentClass()
        : object.getClass();
  }
}
