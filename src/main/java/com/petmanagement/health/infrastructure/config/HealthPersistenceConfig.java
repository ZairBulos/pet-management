package com.petmanagement.health.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.petmanagement.health.infrastructure.adapter.out.persistence.repository"
)
public class HealthPersistenceConfig {
}
