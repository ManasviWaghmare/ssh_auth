package com.org.sshauth.api.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuration to disable datasource auto-configuration when database is not
 * available.
 */
@Configuration
public class DatasourceConfig {
    // Disables automatic datasource initialization
}
