package com.vulinh.annotation.aspect;

import java.lang.annotation.*;

/**
 * Display the execution time of the annotated methods. This annotation is handled by {@link
 * ExecutionTimeAspect}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExecutionTime {}
