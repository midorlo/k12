package com.midorlo.k12.config.actuator;

import org.springframework.boot.actuate.autoconfigure.info.ConditionalOnEnabledInfoContributor;
import org.springframework.boot.actuate.autoconfigure.info.InfoContributorAutoConfiguration;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Application auto-configuration for custom {@link InfoContributor}s.
 */
@Configuration
@AutoConfigureAfter(InfoContributorAutoConfiguration.class)
@ConditionalOnClass(InfoContributor.class)
public class ApplicationContributorConfiguration {

    /**
     * <p>activeProfilesInfoContributor.</p>
     *
     * @param environment a {@link ConfigurableEnvironment} object.
     * @return a {@link ActiveProfilesInfoContributor} object.
     */
    @Bean
    @ConditionalOnEnabledInfoContributor("active-profiles")
    public ActiveProfilesInfoContributor activeProfilesInfoContributor(ConfigurableEnvironment environment) {
        return new ActiveProfilesInfoContributor(environment);
    }
}
