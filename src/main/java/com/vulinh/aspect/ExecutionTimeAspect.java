package com.vulinh.aspect;

import com.vulinh.utils.CommonUtils;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;

/**
 * Please use {@code @}{@link org.springframework.context.annotation.Import}({@link
 * ExecutionTimeAspect}{@code .class)} on your Spring Boot main class to enable this aspect. This
 * class is designed to be pluggable, rather than to be an integrated part of the application
 * context. Then annotate the method with {@code @}{@link com.vulinh.annotation.ExecutionTime}
 */
@Aspect
public class ExecutionTimeAspect {

  final ApplicationContext applicationContext;
  final boolean isVirtualThread;

  static final AtomicReference<ExecutionTimeAspect> SELF = new AtomicReference<>();

  public ExecutionTimeAspect(ApplicationContext applicationContext, Environment environment) {
    this.applicationContext = applicationContext;
    this.isVirtualThread = isVirtualThreadConfigured(environment);
  }

  static final DateTimeFormatter READABLE_TIMESTAMP =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

  static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ExecutionTimeAspect.class);

  @SuppressWarnings("java:S6809") // intentionally self-invocation
  @Around("@annotation(com.vulinh.annotation.ExecutionTime)")
  public Object monitorExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    var startedTimestamp = Instant.now();

    try {
      return joinPoint.proceed();
    } finally {
      if (isVirtualThread) {
        // Proxy invocation
        getSelf().asyncLog(joinPoint, startedTimestamp);
      } else {
        // Self-invocation
        asyncLog(joinPoint, startedTimestamp);
      }
    }
  }

  @Async
  public void asyncLog(ProceedingJoinPoint joinPoint, Instant startedTimestamp) {
    var stoppedTimestamp = Instant.now();

    var signature = joinPoint.getSignature();

    var executionDuration = Duration.between(startedTimestamp, stoppedTimestamp);

    // Invoking conditionally
    if (LOG.isInfoEnabled()) {
      LOG.info(
          "Execution time of method {}.{}({}) is {} ms ({} ns) (from {} to {})",
          signature.getDeclaringTypeName(),
          signature.getName(),
          Arrays.stream(joinPoint.getArgs())
              .<String>mapMulti(
                  (argument, downstream) ->
                      downstream.accept(
                          argument == null ? "null" : argument.getClass().getSimpleName()))
              .collect(Collectors.joining(CommonUtils.COMMA)),
          executionDuration.toMillis(),
          executionDuration.toNanos(),
          toLocalDateTime(startedTimestamp),
          toLocalDateTime(stoppedTimestamp));
    }
  }

  ExecutionTimeAspect getSelf() {
    if (SELF.get() == null) {
      SELF.compareAndSet(null, applicationContext.getBean(ExecutionTimeAspect.class));
    }

    return SELF.get();
  }

  static String toLocalDateTime(Instant startedTimestamp) {
    return "%s UTC"
        .formatted(
            READABLE_TIMESTAMP.format(startedTimestamp.atOffset(ZoneOffset.UTC).toLocalDateTime()));
  }

  static boolean isVirtualThreadConfigured(Environment environment) {
    return Boolean.parseBoolean(environment.getProperty("spring.threads.virtual.enabled"));
  }
}
