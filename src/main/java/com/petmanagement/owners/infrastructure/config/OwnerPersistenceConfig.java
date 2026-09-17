package com.petmanagement.owners.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.petmanagement.owners.infrastructure.adapter.out.persistence.repository"
)
public class OwnerPersistenceConfig {
}
