package com.vulinh.data.base;

import java.time.temporal.Temporal;

/**
 * Base interface for auditable entities with date-time fields.
 *
 * @param <T1> Data type for created date time
 * @param <T2> Data type for updated date time
 */
public interface DateTimeAuditable<T1 extends Temporal, T2 extends Temporal> {

  T1 getCreatedDateTime();

  T2 getUpdatedDateTime();
}
