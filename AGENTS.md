## Overview

This project is a library project intended to provide a single source of truth for data and convenient utility methods.

This library is shared by the `spring-base`, `spring-base-event`, and `spring-base-auth` projects. Keep code in this repository generic, reusable, and stable enough to be consumed by multiple services.

The main source areas are:

* Base data contracts for identifiers, JPA entities, auditing, repositories, mappers, and pageable QueryDSL search.

* Standard API response and error handling support, including `GenericResponse`, application exceptions, common service error codes, and localization lookup.

* Event message contracts and shared event payloads used by service-to-service messaging.

* Utility classes for QueryDSL predicates, JPA specifications, validation chains, JSON handling, UUID generation, ordering, intersected ranges, circular ranges, and Spring cron expression construction.

Most Spring, JPA, QueryDSL, Jackson, logging, P6Spy, and Spring Cloud Stream dependencies are intended as provided or optional integration points. Avoid adding service-specific behavior here; consuming applications should provide concrete configuration, handlers, and business rules.

## Java Formatting Rules

After changing any Java source or test file, run Google Java Format on each changed Java file before final response.

* Whole-file reformatting is acceptable. The formatter does not need to preserve manual wrapping inside the changed Java file.

* Prefer formatting only the Java files changed by the assistant by running `.\.agents\google-java-format-file.cmd path\to\File.java`.

* Multiple changed Java files may be passed to the same command, for example `.\.agents\google-java-format-file.cmd path\to\First.java path\to\Second.java`.

* The per-file formatter script downloads the standalone Google Java Format `all-deps` jar directly from Maven Central into `.agents` when it is missing. Common formatter artifact properties are defined in `.agents/google-java-format.version`, and generated formatter jar and checksum files are ignored by `.agents/.gitignore`.

* The per-file formatter script requires `JAVA_HOME` to point to JDK 25 or newer. It checks `%JAVA_HOME%\bin\java.exe` before invoking Google Java Format. JDK 25 is required so the formatter can parse Java source that uses `import module`.

* If per-file formatting is unavailable, running the project formatter is acceptable. The Maven fallback command is `.\mvnw.cmd com.spotify.fmt:fmt-maven-plugin:format`.

## Java Code Conventions

### No Lombok Self-Imposed Limitations

To avoid external dependencies and maintain full control over generated code, Lombok is not used in this project.

Therefore, all data classes, including regular classes and Java records, must manually provide the following:

* **Builder Pattern**: A static nested `Builder` class for constructing instances.

* **`toBuilder()` method**: A method that returns a new `Builder` initialized with the current instance's values.

* **"Withers" methods**: Methods, such as `withName(String name)`, that return a new instance of the data class with a specific field updated, leaving the original instance immutable.

* **Chainable setters**: For regular classes with setters, each setter returns the object itself, making setter chaining possible.

### No `null` Collections

For records with collection fields, normalize `null` collections to empty collections in the compact constructor.

Use the factory helpers from `CollectionHelper`:

* `CollectionHelper.emptyListIfNull(List<T> list)` for `List` fields.

* `CollectionHelper.emptySetIfNull(Set<T> set)` for `Set` fields.

* `CollectionHelper.emptyMapIfNull(Map<K, V> map)` for `Map` fields.

Prefer this pattern so record instances never expose `null` for collection fields.

Example compact constructor normalization:

```java
public record Employee(
    UUID id,
    String username,
    Set<Role> roles,
    List<Department> departments,
    Map<String, Object> additionalAttributes) {

  public Employee {
    roles = CollectionHelper.emptySetIfNull(roles);
    departments = CollectionHelper.emptyListIfNull(departments);
    additionalAttributes = CollectionHelper.emptyMapIfNull(additionalAttributes);
  }
}

```

## Markdown Formatting Rules (for Human and AI Agents)

* The user is responsible for formatting Markdown with custom line breaks, so there is no word wrap for a single paragraph.

* All bullet points, ordered and unordered, use `*` for unordered lists, and each level is intentionally separated by one blank line. There should be one whitespace character between the asterisk, or number for ordered lists, and the content.

* No em dash.

## IntelliJ Soft-Wrap Setting

When reading files in IntelliJ, you can enable Soft-Wrap for better readability:

* View -> Active Editor -> Soft-Wrap
