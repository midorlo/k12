package com.midorlo.k12.config;

import com.midorlo.k12.config.application.ApplicationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Configure the usage of ApplicationProperties.
 */
@EnableConfigurationProperties(ApplicationProperties.class)
// Load some properties into the environment from files to make them available for interpolation in application.yaml.
@PropertySources(
    {
        @PropertySource(value = "classpath:git.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:META-INF/build-info.properties", ignoreResourceNotFound = true),
    }
)
public class ApplicationConfiguration {}
