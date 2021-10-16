package com.midorlo.k12.configuration.web;

import com.midorlo.k12.configuration.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.CacheControl;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.concurrent.TimeUnit;

import static com.midorlo.k12.configuration.web.WebMvcConfigurerExt.RESOURCE_LOCATIONS;
import static com.midorlo.k12.configuration.web.WebMvcConfigurerExt.RESOURCE_PATHS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class StaticResourcesWebConfigurerTest {

    public static final int                     MAX_AGE_TEST = 5;
    public              WebMvcConfigurerExt     webMvcConfigurerExt;
    private             ResourceHandlerRegistry resourceHandlerRegistry;
    private             ApplicationProperties   props;

    @BeforeEach
    void setUp() {
        MockServletContext    servletContext     = spy(new MockServletContext());
        WebApplicationContext applicationContext = mock(WebApplicationContext.class);
        resourceHandlerRegistry = spy(new ResourceHandlerRegistry(applicationContext, servletContext));
        props                   = new ApplicationProperties();
        webMvcConfigurerExt     = spy(new WebMvcConfigurerExt(props));
    }

    @Test
    void shouldAppendResourceHandlerAndInitializeIt() {
        webMvcConfigurerExt.addResourceHandlers(resourceHandlerRegistry);

        verify(resourceHandlerRegistry, times(1)).addResourceHandler(RESOURCE_PATHS);
        verify(webMvcConfigurerExt, times(1)).initializeResourceHandler(any(ResourceHandlerRegistration.class));
        for (String testingPath : RESOURCE_PATHS) {
            assertThat(resourceHandlerRegistry.hasMappingForPattern(testingPath)).isTrue();
        }
    }

    @Test
    void shouldInitializeResourceHandlerWithCacheControlAndLocations() {
        CacheControl ccExpected = CacheControl.maxAge(5, TimeUnit.DAYS).cachePublic();
        when(webMvcConfigurerExt.getCacheControl()).thenReturn(ccExpected);
        ResourceHandlerRegistration resourceHandlerRegistration = spy(new ResourceHandlerRegistration(RESOURCE_PATHS));

        webMvcConfigurerExt.initializeResourceHandler(resourceHandlerRegistration);

        verify(webMvcConfigurerExt, times(1)).getCacheControl();
        verify(resourceHandlerRegistration, times(1)).setCacheControl(ccExpected);
        verify(resourceHandlerRegistration, times(1)).addResourceLocations(RESOURCE_LOCATIONS);
    }

    @Test
    void shouldCreateCacheControlWithSpecificConfigurationInProperties() {
        props.getHttp().getCache().setTimeToLiveInDays(MAX_AGE_TEST);
        CacheControl cacheExpected = CacheControl.maxAge(MAX_AGE_TEST, TimeUnit.DAYS).cachePublic();
        assertThat(webMvcConfigurerExt.getCacheControl())
            .extracting(CacheControl::getHeaderValue)
            .isEqualTo(cacheExpected.getHeaderValue());
    }
}
