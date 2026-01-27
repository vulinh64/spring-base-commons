package com.vulinh.data.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base abstract class for auditable entities with Instant date-time fields.
 *
 * @param <I>
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AbstractTimestampAuditableEntity<I extends Serializable>
    extends AbstractEntity<I> implements InstantDateTimeAuditable {

  // Bruh
  @Serial private static final long serialVersionUID = 1L;

  @CreatedDate protected Instant createdDateTime;

  @LastModifiedDate protected Instant updatedDateTime;

  @Override
  public Instant getCreatedDateTime() {
    return createdDateTime;
  }

  @Override
  public Instant getUpdatedDateTime() {
    return updatedDateTime;
  }

}
