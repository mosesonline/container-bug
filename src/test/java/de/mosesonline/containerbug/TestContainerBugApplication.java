package de.mosesonline.containerbug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainerBugApplication {

    public static void main(String[] args) {
        SpringApplication.from(ContainerBugApplication::main).with(TestContainerBugApplication.class).run(args);
    }

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:16.0-alpine3.18")
                .withInitScript("init_schema.sql")
                .withDatabaseName("mrgmbh");
    }

    @Bean
    @Primary
    public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(postgreSQLContainer.getDriverClassName());
        dataSourceBuilder.url(postgreSQLContainer.getJdbcUrl());
        dataSourceBuilder.username(postgreSQLContainer.getUsername());
        dataSourceBuilder.password(postgreSQLContainer.getPassword());
        return dataSourceBuilder.build();
    }
}
