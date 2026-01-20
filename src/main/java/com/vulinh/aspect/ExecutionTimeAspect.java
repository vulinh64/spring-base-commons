package com.vulinh.aspect;

import com.vulinh.utils.CommonUtils;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;

/**
 * Please use {@code @org.springframework.context.annotation.Import(ExecutionTimeAspect.class)} on
 * your Spring Boot main class to enable this aspect. This class is designed to be pluggable, rather
 * than to be an integrated part of the application context.
 */
@Aspect
public class ExecutionTimeAspect {

  static final DateTimeFormatter READABLE_TIMESTAMP =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ExecutionTimeAspect.class);

  @Around("@annotation(com.vulinh.annotation.ExecutionTime)")
  public Object monitorExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    var startedTimestamp = LocalDateTime.now();

    try {
      return joinPoint.proceed();
    } finally {
      var stoppedTimestamp = LocalDateTime.now();

      var signature = joinPoint.getSignature();

      var start = READABLE_TIMESTAMP.format(startedTimestamp);
      var stop = READABLE_TIMESTAMP.format(stoppedTimestamp);

      var executionDuration = Duration.between(startedTimestamp, stoppedTimestamp);

      var parameters =
          Arrays.stream(joinPoint.getArgs())
              .<String>mapMulti(
                  (argument, downstream) ->
                      downstream.accept(
                          argument == null ? "null" : argument.getClass().getSimpleName()))
              .collect(Collectors.joining(CommonUtils.COMMA));

      LOG.info(
          "Execution time of method {}.{}({}) is {} ms ({} ns) (from {} to {})",
          signature.getDeclaringTypeName(),
          signature.getName(),
          parameters,
          executionDuration.toMillis(),
          executionDuration.toNanos(),
          start,
          stop);
    }
  }
}
