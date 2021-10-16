package com.midorlo.k12.config.info;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.info.Info;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.env.MockEnvironment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ActiveProfilesInfoContributorTest {

    @Test
    void activeProfilesShouldBeSetWhenProfilesActivated() {
        ConfigurableEnvironment environment = new MockEnvironment();
        environment.setActiveProfiles("prod");
        environment.setDefaultProfiles("dev", "api-docs");

        ActiveProfilesInfoContributor contributor = new ActiveProfilesInfoContributor(environment);

        Info.Builder builder = new Info.Builder();
        contributor.contribute(builder);
        Info info = builder.build();
        //noinspection unchecked
        assertThat((List<String>) info.get("activeProfiles")).contains("prod");
    }
}
