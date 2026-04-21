package com.vulinh.data.base;

import java.util.UUID;

/**
 * Convenience specialization of {@link Identifiable} fixing the identifier type to {@link UUID},
 * the most common (if not opinionated) primary-key shape across this project.
 *
 * <p>Prefer this over {@code Identifiable<UUID>} on non-record classes to keep the declaration
 * concise and to communicate intent: "this object is keyed by a UUID". For Java records, see {@link
 * RecordUuidIdentifiable} instead.
 */
@FunctionalInterface
public interface UuidIdentifiable extends Identifiable<UUID> {}
