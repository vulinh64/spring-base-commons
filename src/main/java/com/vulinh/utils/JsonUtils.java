package com.vulinh.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Convenient but opinionated utility class that provides JSON-related utility methods via the
 * {@link ObjectMapper} class. It, in theory, mimics the Spring Boot's default {@link ObjectMapper}
 * instance.
 *
 * <p>The underlying mapper is pre-configured with:
 *
 * <ul>
 *   <li>{@link SerializationFeature#WRITE_DATES_AS_TIMESTAMPS} disabled (ISO-8601 date strings)
 *   <li>{@link DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES} disabled
 *   <li>{@link MapperFeature#DEFAULT_VIEW_INCLUSION} disabled
 *   <li>{@link com.fasterxml.jackson.datatype.jsr310.JavaTimeModule} registered
 *   <li>{@link com.fasterxml.jackson.annotation.JsonInclude.Include#NON_NULL} serialization
 *       inclusion
 * </ul>
 */
public class JsonUtils {

  private enum PrintType {
    PLAIN,
    PRETTY
  }

  private JsonUtils() {}

  /**
   * Return the {@link ObjectMapper} instance itself, for more advanced methods that this class
   * didn't provide. Can also be used as the unmodifiable data mapper argument.
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
   * @throws IllegalArgumentException if serialization fails
   */
  public static String toMinimizedJSON(Object object) {
    return toJSONString(object, PrintType.PLAIN);
  }

  /**
   * Serializes the given object to a pretty-printed JSON string with indentation and line breaks.
   *
   * @param object the object to serialize
   * @return the pretty-printed JSON string representation
   * @throws IllegalArgumentException if serialization fails
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
   * @throws IllegalArgumentException if deserialization fails
   */
  public static <T> T toObject(String message, Class<T> clazz) {
    try {
      return MAPPER.readValue(message, clazz);
    } catch (JsonProcessingException e) {
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
   * @throws IllegalArgumentException if deserialization fails
   */
  public static <T> T toObject(String message, TypeReference<T> type) {
    try {
      return MAPPER.readValue(message, type);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("JSON deserialization error", e);
    }
  }

  static String toJSONString(Object object, PrintType printType) {
    try {
      return switch (printType) {
        case PLAIN -> MAPPER.writeValueAsString(object);
        case PRETTY -> PRETTY_WRITER.writeValueAsString(object);
      };
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("JSON serialization error", e);
    }
  }

  static final ObjectMapper MAPPER;
  static final ObjectWriter PRETTY_WRITER;

  static {
    MAPPER =
        JsonMapper.builder()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
            .addModule(new JavaTimeModule())
            .serializationInclusion(Include.NON_NULL)
            .build();

    PRETTY_WRITER =
        MAPPER.writer(
            new DefaultPrettyPrinter()
                .withObjectIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
                .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE));
  }
}
