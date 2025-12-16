package com.vulinh.data.base;

import java.time.Instant;

/** Base interface for auditable entities with Instant date-time fields. */
public interface InstantDateTimeAuditable extends DateTimeAuditable<Instant, Instant> {}
