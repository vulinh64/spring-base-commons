package com.vulinh.locale;

import java.util.Collection;
import java.util.ServiceLoader;

/**
 * SPI for contributing {@link java.util.ResourceBundle} base names to {@link LocalizationSupport}.
 *
 * <p>Each implementation declares the resource bundle base names that its module ships (for example
 * {@code "i18n/messages"}, {@code "i18n/errors"}). At startup, {@link LocalizationSupport}
 * discovers all providers via {@link ServiceLoader} and aggregates their names.
 *
 * <p>To register an implementation, services must:
 *
 * <ol>
 *   <li>Provide a public, no-arg constructor on the implementation class.
 *   <li>Declare it in {@code META-INF/services/com.vulinh.locale.LocalizationBundleProvider}, one
 *       fully-qualified class name per line.
 * </ol>
 *
 * <p>Bundle name resolution follows the standard {@link java.util.ResourceBundle#getBundle(String,
 * java.util.Locale)} contract: the name is the path (without locale suffix or {@code .properties}
 * extension) relative to the classpath root, e.g. {@code i18n/messages} resolves {@code
 * i18n/messages_<locale>.properties}.
 */
@FunctionalInterface
public interface LocalizationBundleProvider {

  /**
   * Returns the resource bundle base names contributed by this provider.
   *
   * @return non-null collection of bundle base names; may be empty, blank or {@code null} entries
   *     are ignored by the consumer
   */
  Collection<String> bundleNames();
}
