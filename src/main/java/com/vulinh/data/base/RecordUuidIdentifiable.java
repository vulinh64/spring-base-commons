package com.vulinh.data.base;

import java.util.UUID;

/**
 * Convenience specialization of {@link RecordIdentifiable} fixing the identifier type to {@link
 * UUID}, the most common (if not opinionated) primary-key shape across this project.
 *
 * <p>Prefer this over {@code RecordIdentifiable<UUID>} in record declarations to keep the signature
 * concise and to communicate intent: "this record is keyed by a UUID".
 *
 * <pre>{@code
 * public record UserResponse(UUID id, String username)
 *     implements RecordUuidIdentifiable {}
 * }</pre>
 */
@FunctionalInterface
public interface RecordUuidIdentifiable extends RecordIdentifiable<UUID> {}
