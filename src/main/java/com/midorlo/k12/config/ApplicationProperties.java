package com.midorlo.k12.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to K 12.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {}
