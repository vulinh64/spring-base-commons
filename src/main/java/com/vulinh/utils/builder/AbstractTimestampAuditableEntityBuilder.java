package com.vulinh.utils.builder;

import com.vulinh.data.base.AbstractEntity;

import java.io.Serializable;

/**
 * Extension of {@link AbstractEntityBuilder}
 *
 * @param <I> Entity's ID type
 * @param <E> Entity type
 * @param <B> Builder type (self type)
 */
public interface AbstractTimestampAuditableEntityBuilder<
        I extends Serializable,
        E extends AbstractEntity<I>,
        B extends AbstractTimestampAuditableEntityBuilder<I, E, B>>
    extends AbstractEntityBuilder<I, E, B> {}
