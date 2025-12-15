package com.vulinh.data.base;

import com.vulinh.data.Identifiable;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;

// https://jpa-buddy.com/blog/hopefully-the-final-article-about-equals-and-hashcode-for-jpa-entities-with-db-generated-ids/
public abstract class AbstractIdentifiable<I extends Serializable> implements Identifiable<I> {

  @Override
  public final boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    var id = getId();

    if (id == null) {
      return false;
    }

    return other instanceof AbstractIdentifiable<?> ai
        && getEffectiveClass(this) == getEffectiveClass(ai)
        && Objects.equals(id, ai.getId());
  }

  @Override
  public final int hashCode() {
    var id = getId();

    return id == null ? getEffectiveClass(this).hashCode() : id.hashCode();
  }

  static Class<?> getEffectiveClass(Object object) {
    return object instanceof HibernateProxy hibernateProxy
        ? hibernateProxy.getHibernateLazyInitializer().getPersistentClass()
        : object.getClass();
  }
}
