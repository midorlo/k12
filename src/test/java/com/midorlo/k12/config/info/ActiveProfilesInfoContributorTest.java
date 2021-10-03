package com.midorlo.k12.config.info;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.info.Info;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.env.MockEnvironment;

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

        assertThat((List<String>) info.get("activeProfiles")).contains("prod");
    }

//    @Test
//    void defaultProfilesShouldBeSetWhenNoProfilesActivated() {
//        ConfigurableEnvironment environment = new MockEnvironment();
//        environment.setDefaultProfiles("dev", "api-docs");
//
//        ActiveProfilesInfoContributor contributor = new ActiveProfilesInfoContributor(environment);
//
//        Info.Builder builder = new Info.Builder();
//        contributor.contribute(builder);
//        Info info = builder.build();
//
//        assertThat((List<String>) info.get("activeProfiles")).contains("dev", "api-docs");
//    }
}
