package com.midorlo.k12.configuration.documentation.customizer;

import springfox.documentation.spring.web.plugins.Docket;

/**
 * Callback interface that can be implemented by init wishing to further customize the
 * {@link Docket} in Springfox.
 */
@FunctionalInterface
public interface SpringfoxCustomizer {
    /**
     * Customize the Springfox Docket.
     *
     * @param docket the Docket to customize
     */
    void customize(Docket docket);
}
