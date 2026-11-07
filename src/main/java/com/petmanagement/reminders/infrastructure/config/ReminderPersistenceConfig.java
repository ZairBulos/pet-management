package com.petmanagement.reminders.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.petmanagement.reminders.infrastructure.adapter.out.persistence.repository"
)
public class ReminderPersistenceConfig {
}
