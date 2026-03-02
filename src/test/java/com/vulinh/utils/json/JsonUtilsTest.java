package com.vulinh.utils.json;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vulinh.utils.JsonUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonUtilsTest {

  record SampleObject(String name, int age) {}

  record ObjectWithNullField(String name, String nickname) {}

  record ObjectWithDate(String name, LocalDate birthDate) {}

  record ObjectWithExtra(String name, int age) {}

  @Test
  void testDelegateShouldReturnNonNullMapper() {
    assertNotNull(JsonUtils.delegate());
  }

  @Test
  void testToMinimizedJSONShouldProduceCompactString() {
    var sample = new SampleObject("Alice", 30);

    var json = JsonUtils.toMinimizedJSON(sample);

    assertEquals(
        """
        {"name":"Alice","age":30}""",
        json);
  }

  @Test
  void testToPrettyJSONShouldProduceIndentedString() {
    var sample = new SampleObject("Alice", 30);

    var json = JsonUtils.toPrettyJSON(sample);

    assertTrue(json.contains("\n"), "Pretty JSON should contain newlines");
    assertTrue(json.contains("\"name\""), "Pretty JSON should contain the field name");
    assertTrue(json.contains("\"Alice\""), "Pretty JSON should contain the field value");
  }

  @Test
  void testToObjectWithClassShouldDeserialize() {
    var json =
        """
        {"name":"Bob","age":25}""";

    var result = JsonUtils.toObject(json, SampleObject.class);

    assertEquals("Bob", result.name());
    assertEquals(25, result.age());
  }

  @Test
  void testToObjectWithTypeReferenceShouldDeserialize() {
    var json =
        """
        [{"name":"Alice","age":30},{"name":"Bob","age":25}]""";

    List<SampleObject> result = JsonUtils.toObject(json, new TypeReference<>() {});

    assertEquals(2, result.size());
    assertEquals("Alice", result.get(0).name());
    assertEquals("Bob", result.get(1).name());
  }

  @Test
  void testToObjectWithClassShouldThrowOnInvalidJSON() {
    var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> JsonUtils.toObject("not valid json", SampleObject.class));

    assertEquals("JSON deserialization error", exception.getMessage());
    assertNotNull(exception.getCause());
  }

  @Test
  void testToObjectWithTypeReferenceShouldThrowOnInvalidJSON() {
    var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> JsonUtils.toObject("not valid json", new TypeReference<SampleObject>() {}));

    assertEquals("JSON deserialization error", exception.getMessage());
    assertNotNull(exception.getCause());
  }

  @Test
  void testToMinimizedJSONShouldThrowOnSerializationError() {
    // Direct self-reference leading to cycle
    var unserializable =
        new Object() {
          @SuppressWarnings("unused")
          public Object getSelf() {
            return this;
          }
        };

    assertThrows(IllegalArgumentException.class, () -> JsonUtils.toMinimizedJSON(unserializable));
  }

  @Test
  void testNullFieldsShouldBeExcludedFromJSON() {
    var obj = new ObjectWithNullField("Alice", null);

    var json = JsonUtils.toMinimizedJSON(obj);

    assertFalse(json.contains("nickname"), "Null fields should not appear in JSON");
    assertEquals(
        """
            {"name":"Alice"}""",
        json);
  }

  @Test
  void testDatesShouldBeSerializedAsISOStrings() {
    var obj = new ObjectWithDate("Alice", LocalDate.of(2000, 1, 15));

    var json = JsonUtils.toMinimizedJSON(obj);

    assertTrue(
        json.contains(
            """
            "2000-01-15"\
            """),
        "Date should be ISO-8601 formatted");
    assertFalse(json.matches(".*\\d{10,}.*"), "Date should not be a numeric timestamp");
  }

  @Test
  void testDatesShouldRoundTripCorrectly() {
    var original = new ObjectWithDate("Alice", LocalDate.of(2000, 1, 15));

    var json = JsonUtils.toMinimizedJSON(original);
    var deserialized = JsonUtils.toObject(json, ObjectWithDate.class);

    assertEquals(original, deserialized);
  }

  @Test
  void testUnknownPropertiesShouldBeIgnored() {
    var json =
        """
        {"name":"Alice","age":30,"unknownField":"value"}""";

    var result = assertDoesNotThrow(() -> JsonUtils.toObject(json, ObjectWithExtra.class));

    assertEquals("Alice", result.name());
    assertEquals(30, result.age());
  }

  @Test
  void testMapperShouldHaveDatesAsTimestampsDisabled() {
    assertFalse(JsonUtils.delegate().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
  }

  @Test
  void testMapperShouldHaveFailOnUnknownPropertiesDisabled() {
    assertFalse(JsonUtils.delegate().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
  }

  @Test
  void testSerializationDeserializationRoundTrip() {
    var original = new SampleObject("Charlie", 42);

    var json = JsonUtils.toMinimizedJSON(original);
    var result = JsonUtils.toObject(json, SampleObject.class);

    assertEquals(original, result);
  }

  @Test
  void testToObjectWithTypeReferenceShouldDeserializeMap() {
    var json =
        """
        {"key1":"value1","key2":"value2"}""";

    Map<String, String> result = JsonUtils.toObject(json, new TypeReference<>() {});

    assertEquals(2, result.size());
    assertEquals("value1", result.get("key1"));
    assertEquals("value2", result.get("key2"));
  }
}
