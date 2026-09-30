package com.petmanagement.auth.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.petmanagement.auth.infrastructure.adapter.out.persistence.repository"
)
public class AuthPersistenceConfig {
}
