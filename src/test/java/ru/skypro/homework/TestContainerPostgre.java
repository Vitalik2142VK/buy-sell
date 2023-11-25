package ru.skypro.homework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {TestContainerPostgre.Initializer.class})
public abstract class TestContainerPostgre {
    @Autowired
    protected MockMvc mockMvc;

    public static PostgreSQLContainer<?> container;
    static {
        container = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("mdb")
                .withUsername("mu")
                .withPassword("mp");
        container.start();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url" + container.getJdbcUrl(),
                    "spring.datasource.username" + container.getUsername(),
                    "spring.datasource.password" + container.getPassword(),
                    "spring.liquibase.enabled=true"
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
