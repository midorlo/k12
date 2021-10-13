package com.midorlo.k12.config.info;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * An {@link InfoContributor} that exposes the list of active spring profiles.
 */
public class ActiveProfilesInfoContributor implements InfoContributor {

    private static final String ACTIVE_PROFILES = "activeProfiles";
    private final List<String> profiles;

    /**
     * <p>Constructor for ActiveProfilesInfoContributor.</p>
     *
     * @param environment a {@link ConfigurableEnvironment} object.
     */
    public ActiveProfilesInfoContributor(ConfigurableEnvironment environment) {
        this.profiles = Arrays.asList(environment.getActiveProfiles());
    }


    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail(ACTIVE_PROFILES, this.profiles);
    }
}
