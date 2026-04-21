package com.vulinh.data.mapper;

import com.vulinh.data.base.AbstractEntity;

// Not annotated with @Mapper, let the actual mapper interface do that

/**
 * The basic entity-to-DTO (and vice versa) base mapper.
 *
 * @param <E> the entity type
 * @param <D> the DTO type
 */
public interface EntityDTOMapper<E extends AbstractEntity<?>, D> {

  /**
   * Map from an input entity to a DTO (create the response).
   *
   * @param entity the input entity.
   * @return the DTO created from the input entity.
   */
  D toDto(E entity);

  /**
   * Map from an input DTO to an entity (create the transient entity).
   *
   * @param dto the input DTO
   * @return the transient entity based on the input DTO.
   */
  E toEntity(D dto);
}
