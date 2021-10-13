package com.midorlo.k12.config.security;

import java.util.concurrent.TimeUnit;

import com.midorlo.k12.config.ApplicationConstants;
import com.midorlo.k12.config.ApplicationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile({ ApplicationConstants.SPRING_PROFILE_PRODUCTION })
public class WebMvcConfigurerExt implements WebMvcConfigurer {

    protected static final String[] RESOURCE_LOCATIONS = new String[] {
        "classpath:/static/",
        "classpath:/static/content/",
        "classpath:/static/i18n/",
    };
    public static final    String[] RESOURCE_PATHS     = new String[] {
        "/*.js",
        "/*.css",
        "/*.svg",
        "/*.png",
        "*.ico",
        "/content/**",
        "/i18n/*",
    };

    private final ApplicationProperties applicationProperties;

    public WebMvcConfigurerExt(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        ResourceHandlerRegistration resourceHandlerRegistration = appendResourceHandler(registry);
        initializeResourceHandler(resourceHandlerRegistration);
    }

    protected ResourceHandlerRegistration appendResourceHandler(ResourceHandlerRegistry registry) {
        return registry.addResourceHandler(RESOURCE_PATHS);
    }

    public void initializeResourceHandler(ResourceHandlerRegistration resourceHandlerRegistration) {
        resourceHandlerRegistration.addResourceLocations(RESOURCE_LOCATIONS).setCacheControl(getCacheControl());
    }

    public CacheControl getCacheControl() {
        return CacheControl.maxAge(getApplicationHttpCacheProperty(), TimeUnit.DAYS).cachePublic();
    }

    private int getApplicationHttpCacheProperty() {
        return applicationProperties.getHttp().getCache().getTimeToLiveInDays();
    }
}