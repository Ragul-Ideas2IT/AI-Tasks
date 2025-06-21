package com.connectinghands.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.connectinghands.repository")
@EnableTransactionManagement
public class DatabaseConfig {
    // Configuration is handled by application.yml
} 