package com.vulinh.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Value;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.json.JsonMapper;

/**
 * Convenient, opinionated JSON utility methods backed by a shared immutable Jackson 3 {@link
 * JsonMapper}.
 *
 * <p>The mapper retains the following Jackson 3 defaults:
 *
 * <ul>
 *   <li>Java Time values are serialized as ISO-8601 strings rather than timestamps.
 *   <li>Unknown input properties are ignored during deserialization.
 *   <li>Properties without an active JSON View are excluded when a view is used.
 * </ul>
 *
 * <p>Jackson 3 provides Java Time support directly in Databind; no separate module registration is
 * required. This utility additionally omits null-valued properties during serialization. The mapper
 * is immutable and thread-safe, so it is shared by every utility method.
 */
public class JsonUtils {

  private enum PrintType {
    PLAIN,
    PRETTY
  }

  private JsonUtils() {}

  /**
   * Returns the immutable shared {@link ObjectMapper} for operations not exposed by this utility.
   * The returned mapper is thread-safe and cannot be reconfigured; use {@link
   * ObjectMapper#rebuild()} to derive a separately configured mapper.
   *
   * @return The {@link ObjectMapper} instance.
   */
  public static ObjectMapper delegate() {
    return MAPPER;
  }

  /**
   * Serializes the given object to a compact (minimized) JSON string with no extra whitespace.
   *
   * @param object the object to serialize
   * @return the compact JSON string representation
   * @throws IllegalArgumentException if Jackson cannot serialize the value
   */
  public static String toMinimizedJSON(Object object) {
    return toJSONString(object, PrintType.PLAIN);
  }

  /**
   * Serializes the given object to a pretty-printed JSON string with indentation and line breaks.
   *
   * @param object the object to serialize
   * @return the pretty-printed JSON string representation
   * @throws IllegalArgumentException if Jackson cannot serialize the value
   */
  public static String toPrettyJSON(Object object) {
    return toJSONString(object, PrintType.PRETTY);
  }

  /**
   * Deserializes a JSON string into an object of the specified class.
   *
   * @param message the JSON string to deserialize
   * @param clazz the target class
   * @param <T> the target type
   * @return the deserialized object
   * @throws IllegalArgumentException if Jackson cannot deserialize the JSON
   */
  public static <T> T toObject(String message, Class<T> clazz) {
    try {
      return MAPPER.readValue(message, clazz);
    } catch (JacksonException e) {
      throw new IllegalArgumentException("JSON deserialization error", e);
    }
  }

  /**
   * Deserializes a JSON string into an object of the type described by the given {@link
   * TypeReference}. This is useful for generic types such as {@code List<MyObject>} or {@code
   * Map<String, Object>}.
   *
   * @param message the JSON string to deserialize
   * @param type the {@link TypeReference} describing the target type
   * @param <T> the target type
   * @return the deserialized object
   * @throws IllegalArgumentException if Jackson cannot deserialize the JSON
   */
  public static <T> T toObject(String message, TypeReference<T> type) {
    try {
      return MAPPER.readValue(message, type);
    } catch (JacksonException e) {
      throw new IllegalArgumentException("JSON deserialization error", e);
    }
  }

  static String toJSONString(Object object, PrintType printType) {
    try {
      return switch (printType) {
        case PLAIN -> MAPPER.writeValueAsString(object);
        case PRETTY -> PRETTY_WRITER.writeValueAsString(object);
      };
    } catch (JacksonException e) {
      throw new IllegalArgumentException("JSON serialization error", e);
    }
  }

  static final ObjectMapper MAPPER;
  static final ObjectWriter PRETTY_WRITER;

  static {
    MAPPER =
        JsonMapper.builder().changeDefaultPropertyInclusion(ignored -> Value.ALL_NON_NULL).build();

    PRETTY_WRITER =
        MAPPER
            .writer()
            .with(
                new DefaultPrettyPrinter()
                    .withObjectIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
                    .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE));
  }
}
