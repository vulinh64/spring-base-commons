package com.vulinh.utils;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.Slf4JLogger;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * Add these lines to your application configuration to enable this custom logger:
 *
 * <pre>{@code
 * decorator.datasource.p6spy.logging=CUSTOM
 * decorator.datasource.p6spy.custom-appender-class=com.vulinh.utils.MyP6SpyLogging
 * application-properties.p6spy.max-length=1500
 * }</pre>
 *
 * <p>The {@value #MAX_LENGTH_PROPERTY_KEY} property controls the maximum length of abbreviated SQL
 * statements. It defaults to {@value #DEFAULT_MAX_LENGTH} when omitted or set to a non-positive
 * value. To pick it up from the Spring {@link Environment}, import {@link Configurer} on your
 * Spring Boot main class: {@code @Import(MyP6SpyLogging.Configurer.class)}.
 */
public class MyP6SpyLogging extends Slf4JLogger {

  public static final String MAX_LENGTH_PROPERTY_KEY = "application-properties.p6spy.max-length";

  public static final int DEFAULT_MAX_LENGTH = 1500;

  private static volatile int actualMaxLength = DEFAULT_MAX_LENGTH;

  private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MyP6SpyLogging.class);

  enum LogCategory {
    ERROR,
    WARN,
    INFO,
    DEBUG,
    BATCH,
    STATEMENT,
    RESULTSET,
    COMMIT,
    ROLLBACK,
    RESULT,
    OUTAGE;

    static final Set<String> SETS =
        Arrays.stream(LogCategory.values()).map(Enum::name).collect(Collectors.toSet());
  }

  @Override
  public void logSQL(
      int connectionId,
      String time,
      long elapsed,
      Category category,
      String prepared,
      String sql,
      String url) {
    if (!LOG.isInfoEnabled()) {
      // Do nothing when log level info is not configured
      return;
    }

    var categoryName = category.getName().toUpperCase();

    if (StringUtils.isBlank(sql)) {
      if (LogCategory.SETS.contains(categoryName)) {
        var message =
            switch (LogCategory.valueOf(categoryName)) {
              case COMMIT -> "Transaction committed";
              case ROLLBACK -> "Transaction rolled back";
              case BATCH -> "Batch executed";
              case OUTAGE -> "Connection outage occurred";
              default -> "...";
            };

        LOG.info("#{} [ {} ] - {}", connectionId, categoryName, message);

        return;
      }

      super.logSQL(connectionId, time, elapsed, category, prepared, sql, url);

      return;
    }

    // For blog post, the post content can be very long
    // We will trim this to the configured maximum length
    LOG.info(StringUtils.abbreviate(sql, actualMaxLength));
  }

  /**
   * Pluggable Spring hook that reads {@value #MAX_LENGTH_PROPERTY_KEY} from the {@link Environment}
   * and applies it to {@link MyP6SpyLogging}. Falls back to {@value #DEFAULT_MAX_LENGTH} when the
   * property is missing or not a positive integer.
   */
  public static class Configurer implements EnvironmentAware {

    // Bridging Spring's instance lifecycle to a p6spy-managed class
    @Override
    @SuppressWarnings({"java:S2696", "AssignmentToStaticFieldFromInstanceMethod"})
    public void setEnvironment(Environment environment) {
      var value = environment.getProperty(MAX_LENGTH_PROPERTY_KEY, Integer.class);

      actualMaxLength = value != null && value > 0 ? value : DEFAULT_MAX_LENGTH;
    }
  }
}
