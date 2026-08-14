package com.vulinh.test;

import org.testcontainers.utility.DockerImageName;

/**
 * Shared Docker image definitions used by the ecosystem's integration-test support. Public to be
 * reusable
 */
public final class BaseDockerImage {

  public static final DockerImageName POSTGRESQL_IMAGE =
      DockerImageName.parse("postgres:18.3-alpine3.23");

  public static final DockerImageName RABBITMQ_IMAGE =
      DockerImageName.parse("rabbitmq:4.2.4-alpine");

  private BaseDockerImage() {
    // Utility class
  }
}
