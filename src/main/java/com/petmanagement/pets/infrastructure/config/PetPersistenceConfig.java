package com.petmanagement.pets.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.petmanagement.pets.infrastructure.adapter.out.persistence.repository;"
)
public class PetPersistenceConfig {
}
