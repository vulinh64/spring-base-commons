package com.vulinh.utils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import java.util.function.BiFunction;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/** A utility class for building QueryDSL Predicates. */
public class PredicateBuilder {

  private PredicateBuilder() {}

  /** The escape character used in LIKE expressions to escape special characters. */
  static final char ESCAPE = '\\';

  /**
   * A conjunction.
   *
   * @return the predicate that is always true
   */
  public static Predicate always() {
    return Expressions.asBoolean(true).isTrue();
  }

  /**
   * A disjunction.
   *
   * @return the predicate that is always false
   */
  public static Predicate never() {
    return always().not();
  }

  /**
   * Creates a case-insensitive LIKE predicate for the given expression and keyword.
   *
   * @param expression A QueryDSL expression representing the field to be searched.
   * @param keyword The keyword to search for within the field.
   * @return A Predicate representing the case-insensitive LIKE condition.
   */
  public static Predicate likeIgnoreCase(Expression<?> expression, String keyword) {
    // Spaces are considered none
    if (StringUtils.isBlank(keyword)) {
      return always();
    }

    var patternKeyword = "%%%s%%".formatted(EscapeCharacter.DEFAULT.escape(keyword));

    if (expression instanceof StringExpression se) {
      return se.likeIgnoreCase(patternKeyword, ESCAPE);
    }

    var stringOperation = Expressions.stringOperation(Ops.STRING_CAST, expression);

    return stringOperation.likeIgnoreCase(patternKeyword, ESCAPE);
  }

  /**
   * Combines multiple predicates using logical AND.
   *
   * @param firstPredicate First predicate
   * @param predicates Other predicates
   * @return The combined predicate
   */
  public static Predicate and(
      @Nullable Predicate firstPredicate, @Nullable Predicate... predicates) {
    return combinePredicates(BooleanBuilder::and, firstPredicate, predicates);
  }

  /**
   * Combines multiple predicates using logical OR.
   *
   * @param firstPredicate First predicate
   * @param predicates Other predicates
   * @return The combined predicate
   */
  public static Predicate or(
      @Nullable Predicate firstPredicate, @Nullable Predicate... predicates) {
    return combinePredicates(BooleanBuilder::or, firstPredicate, predicates);
  }

  /**
   * Gets the field name from a QueryDSL Path expression.
   *
   * @param expression The QueryDSL Path expression
   * @return The field name as a String
   */
  @NonNull
  public static String getFieldName(@NonNull Path<?> expression) {
    return expression.getMetadata().getName();
  }

  /**
   * Combines predicates using the specified combiner function.
   *
   * @param combiner a BiFunction that defines how to combine the predicates
   * @param firstPredicate the first predicate to combine
   * @param otherPredicates additional predicates to combine
   * @return the combined predicate
   */
  static Predicate combinePredicates(
      @NonNull BiFunction<BooleanBuilder, Predicate, Predicate> combiner,
      @Nullable Predicate firstPredicate,
      @Nullable Predicate... otherPredicates) {
    var builder = new BooleanBuilder(firstPredicate);

    if (ArrayUtils.isNotEmpty(otherPredicates)) {
      //noinspection DataFlowIssue
      for (var predicate : otherPredicates) {
        combiner.apply(builder, predicate);
      }
    }

    return builder;
  }
}
