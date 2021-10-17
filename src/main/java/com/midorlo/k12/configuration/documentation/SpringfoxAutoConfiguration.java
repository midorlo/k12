package com.midorlo.k12.configuration.documentation;

import static com.midorlo.k12.configuration.ApplicationConstants.ContextConstants.SPRING_PROFILE_API_DOCS;
import static springfox.documentation.builders.PathSelectors.regex;

import com.midorlo.k12.configuration.ApplicationProperties;
import com.midorlo.k12.configuration.documentation.customizer.ApplicationSpringfoxCustomizer;
import com.midorlo.k12.configuration.documentation.customizer.SpringfoxCustomizer;
import java.nio.ByteBuffer;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.oas.configuration.OpenApiDocumentationConfiguration;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Server;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * Springfox OpenAPI configuration.
 * <p>
 * Warning! When having a lot of REST endpoints, Springfox can become a performance issue.
 * In that case, you can use the "no-api-docs" Spring profile, so that this bean is ignored.
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ ApiInfo.class, BeanValidatorPluginsConfiguration.class, Docket.class })
@Profile(SPRING_PROFILE_API_DOCS)
@AutoConfigureAfter(ApplicationProperties.class)
@Import({ OpenApiDocumentationConfiguration.class, Swagger2DocumentationConfiguration.class, BeanValidatorPluginsConfiguration.class })
public class SpringfoxAutoConfiguration {

    static final String STARTING_MESSAGE = "Starting OpenAPI docs";
    static final String STARTED_MESSAGE = "Started OpenAPI docs in {} ms";
    static final String MANAGEMENT_TITLE_SUFFIX = "Management API";
    static final String MANAGEMENT_GROUP_NAME = "management";
    static final String MANAGEMENT_DESCRIPTION = "Management endpoints documentation";

    private final ApplicationProperties.ApiDocs properties;

    /**
     * <p>Constructor for SpringfoxAutoConfiguration.</p>
     *
     * @param applicationProperties a {@link ApplicationProperties} object.
     */
    public SpringfoxAutoConfiguration(ApplicationProperties applicationProperties) {
        this.properties = applicationProperties.getApiDocs();
    }

    /**
     * Springfox configuration for the OpenAPI docs.
     *
     * @param springfoxCustomizers Springfox customizers
     * @param alternateTypeRules   alternate type rules
     * @return the Springfox configuration
     */
    @Bean
    @ConditionalOnMissingBean(name = "openAPISpringfoxApiDocket")
    public Docket openAPISpringfoxApiDocket(
        List<SpringfoxCustomizer> springfoxCustomizers,
        ObjectProvider<AlternateTypeRule[]> alternateTypeRules
    ) {
        log.debug(STARTING_MESSAGE);
        StopWatch watch = new StopWatch();
        watch.start();

        Docket docket = createDocket();

        // Apply all OpenAPICustomizers orderly.
        springfoxCustomizers.forEach(customizer -> customizer.customize(docket));

        // Add all AlternateTypeRules if available in spring bean factory.
        // Also, you can add your rules in a customizer bean above.
        Optional.ofNullable(alternateTypeRules.getIfAvailable()).ifPresent(docket::alternateTypeRules);

        watch.stop();
        log.debug(STARTED_MESSAGE, watch.getTotalTimeMillis());
        return docket;
    }

    /**
     * Application Springfox Customizer
     *
     * @return the Sringfox Customizer of Application
     */
    @Bean
    public ApplicationSpringfoxCustomizer applicationSpringfoxCustomizer() {
        return new ApplicationSpringfoxCustomizer(properties);
    }

    /**
     * Springfox configuration for the management endpoints (actuator) OpenAPI docs.
     *
     * @param appName the application name
     * @return the Springfox configuration
     */
    @Bean
    @ConditionalOnClass(name = "org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties")
    @ConditionalOnMissingBean(name = "openAPISpringfoxManagementDocket")
    public Docket openAPISpringfoxManagementDocket(@Value("${spring.application.name:application}") String appName) {
        ApiInfo apiInfo = new ApiInfo(
            StringUtils.capitalize(appName) + " " + MANAGEMENT_TITLE_SUFFIX,
            MANAGEMENT_DESCRIPTION,
            properties.getVersion(),
            "",
            ApiInfo.DEFAULT_CONTACT,
            "",
            "",
            new ArrayList<>()
        );

        Docket docket = createDocket();

        for (ApplicationProperties.ApiDocs.Server server : properties.getServers()) {
            docket.servers(
                new Server(server.getName(), server.getUrl(), server.getDescription(), Collections.emptyList(), Collections.emptyList())
            );
        }

        docket =
            docket
                .apiInfo(apiInfo)
                .useDefaultResponseMessages(properties.isUseDefaultResponseMessages())
                .groupName(MANAGEMENT_GROUP_NAME)
                .host(properties.getHost())
                .protocols(new HashSet<>(Arrays.asList(properties.getProtocols())))
                .forCodeGeneration(true)
                .directModelSubstitute(ByteBuffer.class, String.class)
                .genericModelSubstitutes(ResponseEntity.class);

        // ignore Pageable parameter only if the class is present
        if (ClassUtils.isPresent("org.springframework.data.domain.Pageable", SpringfoxAutoConfiguration.class.getClassLoader())) {
            docket = docket.ignoredParameterTypes(org.springframework.data.domain.Pageable.class);
        }

        return docket.select().paths(regex(properties.getManagementIncludePattern())).build();
    }

    /**
     * <p>createDocket.</p>
     *
     * @return a {@link Docket} object.
     */
    protected Docket createDocket() {
        return new Docket(DocumentationType.OAS_30);
    }
}
