package com.vulinh.utils;

import com.vulinh.data.base.Identifiable;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// To not conflict with other CollectionUtils from common libraries
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
}
