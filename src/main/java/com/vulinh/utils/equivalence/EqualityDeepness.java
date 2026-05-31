package com.vulinh.utils.equivalence;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Defines the strategy for comparing extracted ID objects and calculating their hash codes. This
 * allows for choosing between shallow or deep equality semantics, particularly for arrays.
 */
public enum EqualityDeepness {

  /**
   * Uses {@code equals(Object, Object)} for ID comparison and {@code hashCode(Object)} for hash
   * code calculation. This performs a shallow comparison for arrays (i.e., compares array
   * references, not contents).
   */
  SHALLOW_EQUAL(Objects::equals, Objects::hashCode),

  /**
   * Uses {@code deepEquals(Object, Object)} for ID comparison and a custom deep hash code
   * calculation ({@link #handleGetHashCode(Object)}) for arrays. This is suitable for IDs that are
   * arrays where content equality is desired.
   */
  DEEP_EQUAL(Objects::deepEquals, EqualityDeepness::handleGetHashCode);

  EqualityDeepness(BiPredicate<Object, Object> idComparator, ToIntFunction<Object> hashCalculator) {
    this.idComparator = idComparator;
    this.hashCalculator = hashCalculator;
  }

  /**
   * Calculates a hash code for an object, with special handling for arrays. For arrays, it uses the
   * appropriate {@code Arrays.hashCode} for primitive arrays or {@code deepHashCode(Object[])} for
   * object arrays to ensure content-based hashing. For non-array objects, it delegates to {@code
   * hashCode(Object)}.
   *
   * @param object The object to hash.
   * @return The hash code.
   */
  static int handleGetHashCode(Object object) {
    return object.getClass().isArray()
        ? switch (object) {
          case int[] intArray -> Arrays.hashCode(intArray);
          case long[] longArray -> Arrays.hashCode(longArray);
          case byte[] byteArray -> Arrays.hashCode(byteArray);
          case short[] shortArray -> Arrays.hashCode(shortArray);
          case boolean[] booleanArray -> Arrays.hashCode(booleanArray);
          case char[] charArray -> Arrays.hashCode(charArray);
          case double[] doubleArray -> Arrays.hashCode(doubleArray);
          case float[] floatArray -> Arrays.hashCode(floatArray);
          default -> Arrays.deepHashCode((Object[]) object);
        }
        : Objects.hashCode(object);
  }

  /** The predicate used to compare two ID objects for equality. */
  final BiPredicate<Object, Object> idComparator;

  /** The function used to calculate the hash code of an ID object. */
  final ToIntFunction<Object> hashCalculator;

  /**
   * Compares two ID objects for equality using the configured strategy.
   *
   * @param id1 The first ID object.
   * @param id2 The second ID object.
   * @return {@code true} if the IDs are considered equal, {@code false} otherwise.
   */
  boolean compareId(Object id1, Object id2) {
    return idComparator.test(id1, id2);
  }

  /**
   * Calculates the hash code of an ID object using the configured strategy.
   *
   * @param object The ID object.
   * @return The hash code.
   */
  int calculateHashCode(Object object) {
    return hashCalculator.applyAsInt(object);
  }
}
