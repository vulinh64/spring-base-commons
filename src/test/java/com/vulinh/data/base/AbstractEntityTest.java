package com.vulinh.data.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.vulinh.exception.ConcreteEntityIdMissingException;
import com.vulinh.utils.JpaEntityUtils.IdType;
import java.io.Serial;
import java.util.HashMap;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AbstractEntityTest {

  private static final UUID DYNAMIC_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final String CONCRETE_ID = "concrete-key";

  @Test
  void dynamicEntities_withSameId_resolveToSameHashMapEntry() {
    var map = new HashMap<DynamicEntity, String>();
    map.put(new DynamicEntity(DYNAMIC_ID), "stored");

    assertEquals("stored", map.get(new DynamicEntity(DYNAMIC_ID)));
  }

  @Test
  void dynamicEntities_withNullIds_remainSeparateHashMapEntries() {
    var map = new HashMap<DynamicEntity, String>();
    map.put(new DynamicEntity(null), "first");
    map.put(new DynamicEntity(null), "second");

    assertEquals(2, map.size());
    // Lookup with a fresh transient instance also misses — transient entities are never equal
    assertNull(map.get(new DynamicEntity(null)));
  }

  @Test
  void concreteEntities_withSamePresetId_resolveToSameHashMapEntry() {
    var map = new HashMap<ConcreteEntity, String>();
    map.put(new ConcreteEntity(CONCRETE_ID), "stored");

    assertEquals("stored", map.get(new ConcreteEntity(CONCRETE_ID)));
  }

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  @Test
  void concreteEntity_withNullId_throwsOnHashMapInsertion() {
    var map = new HashMap<ConcreteEntity, String>();
    var orphan = new ConcreteEntity(null);

    assertThrows(ConcreteEntityIdMissingException.class, () -> map.put(orphan, "stored"));
  }

  private static final class DynamicEntity extends AbstractEntity<UUID> {

    @Serial private static final long serialVersionUID = 0L;

    private final UUID id;

    private DynamicEntity(UUID id) {
      this.id = id;
    }

    @Override
    public UUID getId() {
      return id;
    }
  }

  private static final class ConcreteEntity extends AbstractEntity<String> {

    @Serial private static final long serialVersionUID = 0L;

    private final String id;

    private ConcreteEntity(String id) {
      this.id = id;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public IdType getIdType() {
      return IdType.CONCRETE;
    }
  }
}
