package com.vulinh.data.base;

import java.time.temporal.Temporal;

/**
 * Base interface for auditable entities with date-time fields.
 *
 * @param <T> Data type for created and updated date time
 */
public interface DateTimeAuditable<T extends Temporal> {

  T getCreatedDateTime();

  T getUpdatedDateTime();
}
