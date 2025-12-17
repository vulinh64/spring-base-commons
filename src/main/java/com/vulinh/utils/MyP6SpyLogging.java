package com.vulinh.utils;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.Slf4JLogger;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

/**
 * Add those lines to your application properties file (properties, YAML, etc...) to enable this
 * custom logger:
 *
 * <p><code>
 * <pre>decorator.datasource.p6spy.logging: CUSTOM<br />decorator.datasource.p6spy.custom-appender-class: com.vulinh.utils.MyP6SpyLogging</pre>
 * </code>
 */
public class MyP6SpyLogging extends Slf4JLogger {

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

    LOG.info(sql);
  }
}
