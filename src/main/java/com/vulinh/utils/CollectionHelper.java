package com.vulinh.utils;

import com.vulinh.data.base.Identifiable;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Provides a set of utility methods for common collection operations, aiming to simplify and
 * streamline collection handling across various services.
 *
 * <p>This class is named {@code CollectionHelper} instead of {@code CollectionUtils} to prevent
 * naming conflicts with similar utility classes found in popular libraries like Spring, Apache
 * Commons, and Guava. This naming convention helps avoid ambiguity, especially when using wildcard
 * imports.
 */
public class CollectionHelper {

  private CollectionHelper() {}

  /**
   * Convert a collection of Identifiable objects into a Map where the keys are the IDs and the
   * values are the objects themselves.
   *
   * @param collection the collection of Identifiable objects
   * @return a Map with IDs as keys and Identifiable objects as values
   * @param <ID> the type of the identifier
   * @param <OBJ> the type of the Identifiable objects
   */
  public static <ID extends Serializable, OBJ extends Identifiable<ID>> Map<ID, OBJ> toMap(
      Collection<? extends OBJ> collection) {
    return collection.stream().collect(Collectors.toMap(Identifiable::getId, Function.identity()));
  }

  /**
   * Returns the given list, or {@link Collections#emptyList()} if it is {@code null}.
   *
   * <p>Convenience over the generic {@link #emptyCollectionIfNull(Collection, Supplier)} for the
   * common {@link List} case, so callers don't need to spell out the empty-list supplier.
   *
   * @param list the list, possibly {@code null}
   * @param <T> the element type
   * @return {@code list} if non-null, otherwise an immutable empty list
   */
  public static <T> List<T> emptyListIfNull(List<T> list) {
    return emptyCollectionIfNull(list, Collections::emptyList);
  }

  /**
   * Returns the given set, or {@link Collections#emptySet()} if it is {@code null}.
   *
   * <p>Convenience over the generic {@link #emptyCollectionIfNull(Collection, Supplier)} for the
   * common {@link Set} case, so callers don't need to spell out the empty-set supplier.
   *
   * @param set the set, possibly {@code null}
   * @param <T> the element type
   * @return {@code set} if non-null, otherwise an immutable empty set
   */
  public static <T> Set<T> emptySetIfNull(Set<T> set) {
    return emptyCollectionIfNull(set, Collections::emptySet);
  }

  /**
   * Returns the given map, or {@link Collections#emptyMap()} if it is {@code null}.
   *
   * @param map the map, possibly {@code null}
   * @param <K> the key type
   * @param <V> the value type
   * @return {@code map} if non-null, otherwise an immutable empty map
   */
  public static <K, V> Map<K, V> emptyMapIfNull(Map<K, V> map) {
    return map == null ? Collections.emptyMap() : map;
  }

  /**
   * Returns the given collection, or the value produced by {@code emptySupplier} if it is {@code
   * null}.
   *
   * <p>Generic null-coalescing helper that lets callers protect any collection-typed field or
   * parameter with a single call. Prefer the dedicated {@link #emptyListIfNull(List)} and {@link
   * #emptySetIfNull(Set)} overloads for the common cases; reach for this one when working with a
   * less common collection type (e.g. {@link Queue}, {@link ArrayDeque}) or when the caller already
   * has a fitting empty-collection supplier on hand.
   *
   * @param collection the collection, possibly {@code null}
   * @param emptySupplier supplier invoked only when {@code collection} is {@code null}
   * @param <C> the concrete collection type
   * @return {@code collection} if non-null, otherwise {@code emptySupplier.get()}
   */
  public static <C extends Collection<?>> C emptyCollectionIfNull(
      C collection, Supplier<? extends C> emptySupplier) {
    return collection == null
        ? CommonUtils.throwsIfNull(
            emptySupplier.get(), () -> new IllegalArgumentException("Empty value from supplier"))
        : collection;
  }
}
