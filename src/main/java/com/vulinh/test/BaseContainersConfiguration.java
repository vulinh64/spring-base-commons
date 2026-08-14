package com.vulinh.test;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;

/**
 * Reusable container bean definitions for integration tests.
 *
 * <p>This class is deliberately not a Spring configuration. Applications opt in by creating an
 * {@code @TestConfiguration} subclass and importing that subclass into a test context. This keeps
 * PostgreSQL and RabbitMQ containers dormant in tests that do not request them.
 *
 * <p>A subclass may override a container bean to add application-specific options. Repeat both
 * {@code @Bean} and {@code @ServiceConnection} on the override:
 *
 * <pre>{@code
 * @Bean
 * @ServiceConnection
 * @Override
 * protected PostgreSQLContainer postgresqlContainer() {
 *   return super.postgresqlContainer().withDatabaseName("application-test");
 * }
 * }</pre>
 */
public abstract class BaseContainersConfiguration {

  @Bean
  @ServiceConnection
  protected PostgreSQLContainer postgresqlContainer() {
    return new PostgreSQLContainer(BaseDockerImage.POSTGRESQL_IMAGE);
  }

  @Bean
  @ServiceConnection
  protected RabbitMQContainer rabbitmqContainer() {
    return new RabbitMQContainer(BaseDockerImage.RABBITMQ_IMAGE);
  }
}
