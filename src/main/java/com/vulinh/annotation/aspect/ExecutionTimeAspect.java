package com.vulinh.annotation.aspect;

import com.vulinh.utils.CommonUtils;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

/**
 * Please use {@code @}{@link org.springframework.context.annotation.Import}({@link
 * ExecutionTimeAspect}{@code .class)} on your Spring Boot main class to enable this aspect. This
 * class is designed to be pluggable, rather than to be an integrated part of the application
 * context. Then annotate the method with {@code @}{@link
 * com.vulinh.annotation.aspect.ExecutionTime}.
 */
@Aspect
public class ExecutionTimeAspect {

  static final Logger LOG = LoggerFactory.getLogger(ExecutionTimeAspect.class);

  static final DateTimeFormatter READABLE_TIMESTAMP =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

  final boolean isVirtualThreadSupport;

  public ExecutionTimeAspect(Environment environment) {
    isVirtualThreadSupport = checkVirtualThreadSupport(environment);
  }

  @Around("@annotation(com.vulinh.annotation.aspect.ExecutionTime)")
  public Object monitorExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    var startedTimestamp = Instant.now();

    try {
      return joinPoint.proceed();
    } finally {
      if (isVirtualThreadSupport) {
        // Fire a virtual thread directly
        Thread.ofVirtual().unstarted(() -> asyncLog(joinPoint, startedTimestamp)).start();
      } else {
        // Self-invocation
        asyncLog(joinPoint, startedTimestamp);
      }
    }
  }

  private void asyncLog(ProceedingJoinPoint joinPoint, Instant startedTimestamp) {
    var stoppedTimestamp = Instant.now();

    var signature = joinPoint.getSignature();

    LOG.atInfo()
        .setMessage("Execution time of method {}.{}({}) is {} ms ({} ns) (from {} to {})")
        .addArgument(signature.getDeclaringTypeName())
        .addArgument(signature.getName())
        .addArgument(
            () ->
                Arrays.stream(joinPoint.getArgs())
                    .<String>mapMulti(
                        (argument, downstream) ->
                            downstream.accept(
                                argument == null ? "null" : argument.getClass().getSimpleName()))
                    .collect(Collectors.joining(CommonUtils.COMMA)))
        .addArgument(() -> Duration.between(startedTimestamp, stoppedTimestamp).toMillis())
        .addArgument(() -> Duration.between(startedTimestamp, stoppedTimestamp).toNanos())
        .addArgument(() -> toLocalDateTime(startedTimestamp))
        .addArgument(() -> toLocalDateTime(stoppedTimestamp))
        .log();
  }

  static String toLocalDateTime(Instant startedTimestamp) {
    return "%s UTC"
        .formatted(
            READABLE_TIMESTAMP.format(startedTimestamp.atOffset(ZoneOffset.UTC).toLocalDateTime()));
  }

  static boolean checkVirtualThreadSupport(Environment environment) {
    return Boolean.parseBoolean(environment.getProperty("spring.threads.virtual.enabled"))
        || Thread.currentThread().isVirtual();
  }
}
