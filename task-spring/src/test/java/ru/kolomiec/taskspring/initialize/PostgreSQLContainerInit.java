package ru.kolomiec.taskspring.initialize;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSQLContainerInit {

    public static final PostgreSQLContainer container = new PostgreSQLContainer("postgres:10")
            .withUsername("root")
            .withPassword("root")
            .withDatabaseName("test-schema");


    public static class InitializeApplicationContextWithDBProperties
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + container.getJdbcUrl(),
                    "spring.datasource.username=" +container.getUsername(),
                    "spring.datasource.password=" +container.getPassword()
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
