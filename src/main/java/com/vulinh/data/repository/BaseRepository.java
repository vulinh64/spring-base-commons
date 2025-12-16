package com.vulinh.data.repository;

import java.io.Serializable;

import com.vulinh.data.base.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * A convenient base repository interface that combines several Spring Data interfaces for common
 * data access patterns.
 *
 * @param <E> Entity type, extending {@link AbstractEntity}
 * @param <I> Identifier type of the entity, extending {@link Serializable}
 */
@NoRepositoryBean
public interface BaseRepository<E extends AbstractEntity<I>, I extends Serializable>
    extends JpaRepository<E, I>,
        JpaSpecificationExecutor<E>,
        ListQuerydslPredicateExecutor<E>,
        QueryByExampleExecutor<E> {}
