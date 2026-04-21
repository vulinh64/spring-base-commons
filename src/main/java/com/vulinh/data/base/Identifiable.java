package com.vulinh.data.base;

import java.io.Serializable;

/**
 * Root contract for any object that can be uniquely identified by a representative ID.
 *
 * <p>This covers the shapes we have found most broadly useful and leaves richer specializations
 * (composite keys, tenant-scoped identifiers, non-UUID primary keys, and so on) to the consuming
 * application to add on top.
 *
 * <h2>Interface hierarchy at a glance</h2>
 *
 * <pre>
 * Identifiable&lt;I&gt;                  root contract, JavaBean-style {@link #getId()}
 * &nbsp;├── {@link UuidIdentifiable}                specialization fixing {@code I} to {@link java.util.UUID}
 * &nbsp;└── {@link RecordIdentifiable}&lt;I&gt;          record-flavored variant exposing {@code id()}; {@code getId()} is bridged by default
 * &nbsp;     └── {@link RecordUuidIdentifiable}     {@link RecordIdentifiable} fixed to {@link java.util.UUID}
 * </pre>
 *
 * <h2>Which one to pick</h2>
 *
 * <ul>
 *   <li>A Java <strong>record</strong> (typically a DTO or projection): implement {@link
 *       RecordIdentifiable} (or {@link RecordUuidIdentifiable} when the key is a UUID). The
 *       record's canonical {@code id()} accessor satisfies the contract automatically.
 *   <li>Any other POJO that simply needs to declare "I have an id": implement {@link Identifiable}
 *       (or {@link UuidIdentifiable}) directly.
 * </ul>
 *
 * <h2>Extending the hierarchy</h2>
 *
 * <p>Consumers are encouraged to introduce their own sub-interfaces when a project-specific pattern
 * emerges; for example, a {@code TenantScopedIdentifiable} that pairs {@code getId()} with a tenant
 * discriminator, or a {@code LongIdentifiable} for legacy schemas using numeric primary keys.
 * Keeping those specializations close to the application's domain avoids polluting this library
 * with opinions that do not generalize.
 *
 * @param <I> the type of the identifier; must be {@link Serializable} so implementers can be safely
 *     persisted, cached, or transported across process boundaries.
 */
@FunctionalInterface
public interface Identifiable<I extends Serializable> {

  /**
   * Gets the unique identifier of the object.
   *
   * @return the unique identifier.
   */
  I getId();
}
