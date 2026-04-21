package com.vulinh.locale;

import com.vulinh.utils.validator.ApplicationError;
import java.text.MessageFormat;
import java.util.*;
import java.util.ResourceBundle.Control;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;

/**
 * Utility for resolving and interpolating localized messages from {@link ResourceBundle resource
 * bundles}, using the locale provided by Spring's {@link LocaleContextHolder}.
 *
 * <p>Bundle base names are not hardcoded, as this is a library, so each consuming service registers
 * its own bundles by implementing {@link LocalizationBundleProvider} and declaring it in {@code
 * META-INF/services/com.vulinh.locale.LocalizationBundleProvider}. All providers are aggregated at
 * class load via {@link ServiceLoader}.
 *
 * <p>Messages are looked up by code (typically the error code of an {@link ApplicationError}) and
 * interpolated using {@link MessageFormat#format(String, Object...)} semantics. If the code cannot
 * be resolved or interpolation fails, the code itself is returned as a safe fallback.
 */
public final class LocalizationSupport {

  private static final Logger LOG = LoggerFactory.getLogger(LocalizationSupport.class);

  static final String BASE_NAME = "i18n";

  static final Control BUNDLE_CONTROL = new MultiResourceBundleControl(loadBundleNames());

  private static List<String> loadBundleNames() {
    var names =
        ServiceLoader.load(LocalizationBundleProvider.class).stream()
            .<String>mapMulti(
                (provider, downstream) -> {
                  var bundleNames = provider.get().bundleNames();

                  if (bundleNames == null) {
                    return;
                  }

                  for (var bundleName : bundleNames) {
                    if (StringUtils.isNotBlank(bundleName)) {
                      downstream.accept(bundleName);
                    }
                  }
                })
            .distinct()
            .toList();

    if (names.isEmpty()) {
      LOG.warn(
          "No {} services registered; localized messages will fall back to raw codes.",
          LocalizationBundleProvider.class.getName());
    } else {
      LOG.debug("Registered localization bundles: {}", names);
    }

    return names;
  }

  /**
   * Resolves and interpolates the localized message for the given {@link ApplicationError}.
   *
   * <p>Equivalent to {@link #getParsedMessage(String, Object...)} called with the error's {@link
   * ApplicationError#getErrorCode() code}.
   *
   * @param applicationError the application error whose code identifies the message to resolve
   * @param args optional positional arguments for {@link MessageFormat#format(String, Object...)}
   *     interpolation (referenced in the bundle entry as {@code {0}}, {@code {1}}, …)
   * @return the resolved and interpolated message, or the error code if resolution fails
   */
  public static String getParsedMessage(ApplicationError applicationError, Object... args) {
    return getParsedMessage(applicationError.getErrorCode(), args);
  }

  /**
   * Resolves and interpolates the localized message for the given message code.
   *
   * <p>The bundle is loaded for the locale returned by {@link LocaleContextHolder#getLocale()}. The
   * raw message is interpolated with {@code args} via {@link MessageFormat#format(String,
   * Object...)} when any arguments are supplied: placeholders use the {@code {0}}, {@code {1}}, …
   * positional syntax (not {@code %s} / {@link String#formatted(Object...)}). If the code is
   * missing, the resolved value is blank, or any error occurs while loading the bundle or
   * formatting, the {@code code} itself is returned as a fallback.
   *
   * @param code the message code (key) to look up in the resource bundle
   * @param args optional positional arguments for {@link MessageFormat#format(String, Object...)}
   *     interpolation (referenced in the bundle entry as {@code {0}}, {@code {1}}, …)
   * @return the resolved and interpolated message, or {@code code} if resolution fails
   */
  public static String getParsedMessage(String code, Object... args) {
    try {
      var rawMessage =
          ResourceBundle.getBundle(BASE_NAME, LocaleContextHolder.getLocale(), BUNDLE_CONTROL)
              .getString(code);

      if (StringUtils.isBlank(rawMessage)) {
        return code;
      }

      return ArrayUtils.isEmpty(args) ? rawMessage : MessageFormat.format(rawMessage, args);
    } catch (Exception exception) {
      LOG.atWarn()
          .setMessage("Localization error: {}")
          .addArgument(exception.getMessage())
          .setCause(exception)
          .log();

      return code;
    }
  }

  private LocalizationSupport() {
    throw new UnsupportedOperationException("No instantiation");
  }

  static class MultiResourceBundleControl extends Control {

    final Collection<String> bundleNames;

    MultiResourceBundleControl(Collection<String> bundleNames) {
      this.bundleNames = bundleNames;
    }

    @Override
    public ResourceBundle newBundle(
        String baseName, Locale locale, String format, ClassLoader loader, boolean reload) {
      return new MultiResourceBundle(
          bundleNames.stream()
              .filter(StringUtils::isNotBlank)
              .map(bundleName -> ResourceBundle.getBundle(bundleName, locale))
              .toList());
    }

    static class MultiResourceBundle extends ResourceBundle {

      final Collection<ResourceBundle> resourceBundles;

      MultiResourceBundle(Collection<ResourceBundle> resourceBundles) {
        this.resourceBundles = resourceBundles;
      }

      @Override
      public Object handleGetObject(@NonNull String key) {
        return resourceBundles.stream()
            .filter(Objects::nonNull)
            .filter(bundle -> bundle.containsKey(key))
            .map(e -> e.getObject(key))
            .findFirst()
            .orElse(null);
      }

      @Override
      @NonNull
      public Enumeration<String> getKeys() {
        return Collections.enumeration(
            resourceBundles.stream()
                .filter(Objects::nonNull)
                .map(ResourceBundle::getKeys)
                .map(Collections::list)
                .flatMap(Collection::stream)
                .toList());
      }
    }
  }
}
