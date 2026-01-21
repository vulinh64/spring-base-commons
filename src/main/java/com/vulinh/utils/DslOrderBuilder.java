package com.vulinh.utils;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.OrderSpecifier.NullHandling;
import java.util.Objects;

/**
 * Small helper to build {@link OrderSpecifier} instances for a given Querydsl {@link Expression}.
 *
 * <p>Provides convenient factory methods for ascending/descending order with explicit nulls
 * handling. When not specified, the default nulls placement is {@link NullHandling#NullsLast}.
 *
 * @param <T> value type of the expression; must be {@link Comparable}, as specified in {@link
 *     OrderSpecifier}.
 */
public final class DslOrderBuilder<T extends Comparable<? super T>> {

  /** Default nulls handling when not explicitly specified. */
  private static final NullHandling DEFAULT_NULL_HANDLING = NullHandling.NullsLast;

  /** The expression to order by. */
  private final Expression<T> field;

  /**
   * Static factory method to start building an order specifier.
   *
   * @param <T> value type of the expression
   * @param field the expression to order by; must not be {@code null}
   * @return a new {@code QOrderBuilder} for the provided expression
   * @throws NullPointerException if {@code field} is {@code null}
   */
  public static <T extends Comparable<? super T>> DslOrderBuilder<T> fromField(
      Expression<T> field) {
    return new DslOrderBuilder<>(field);
  }

  /**
   * Ascending order using the default nulls handling ({@link #DEFAULT_NULL_HANDLING}).
   *
   * @return ascending {@link OrderSpecifier} with default nulls placement
   */
  public OrderSpecifier<T> withAsc() {
    return buildOrder(Order.ASC, DEFAULT_NULL_HANDLING);
  }

  /**
   * Descending order using the default nulls handling ({@link #DEFAULT_NULL_HANDLING}).
   *
   * @return descending {@link OrderSpecifier} with default nulls placement
   */
  public OrderSpecifier<T> withDesc() {
    return buildOrder(Order.DESC, DEFAULT_NULL_HANDLING);
  }

  /**
   * Ascending order with null values placed first.
   *
   * @return ascending {@link OrderSpecifier} with {@link NullHandling#NullsFirst}
   */
  public OrderSpecifier<T> withAscNullFirst() {
    return buildOrder(Order.ASC, NullHandling.NullsFirst);
  }

  /**
   * Ascending order with null values placed last.
   *
   * @return ascending {@link OrderSpecifier} with {@link NullHandling#NullsLast}
   */
  public OrderSpecifier<T> withAscNullLast() {
    return buildOrder(Order.ASC, NullHandling.NullsLast);
  }

  /**
   * Descending order with null values placed first.
   *
   * @return descending {@link OrderSpecifier} with {@link NullHandling#NullsFirst}
   */
  public OrderSpecifier<T> withDescNullFirst() {
    return buildOrder(Order.DESC, NullHandling.NullsFirst);
  }

  /**
   * Descending order with null values placed last.
   *
   * @return descending {@link OrderSpecifier} with {@link NullHandling#NullsLast}
   */
  public OrderSpecifier<T> withDescNullLast() {
    return buildOrder(Order.DESC, NullHandling.NullsLast);
  }

  private OrderSpecifier<T> buildOrder(Order direction, NullHandling nullHandling) {
    return new OrderSpecifier<>(direction, field, nullHandling);
  }

  private DslOrderBuilder(Expression<T> field) {
    this.field = Objects.requireNonNull(field, "Field cannot be null");
  }
}
