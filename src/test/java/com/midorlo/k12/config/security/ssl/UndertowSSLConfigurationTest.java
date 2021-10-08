package com.midorlo.k12.config.security.ssl;

import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.xnio.OptionMap;

import static org.assertj.core.api.Assertions.assertThat;

class UndertowSSLConfigurationTest {

    @Test
    void testUndertowSSLConfigurationOK() {
        //Prepare
        UndertowServletWebServerFactory undertowServletWebServerFactory = new UndertowServletWebServerFactory();

        //Execute
        UndertowSSLConfiguration undertowSSLConfiguration = new UndertowSSLConfiguration(undertowServletWebServerFactory);

        //Verify
        Undertow.Builder builder = Undertow.builder();
        undertowServletWebServerFactory.getBuilderCustomizers().forEach(c -> c.customize(builder));
        OptionMap.Builder serverOptions = (OptionMap.Builder) ReflectionTestUtils.getField(builder, "socketOptions");
        assertThat(undertowServletWebServerFactory).isNotNull();
        assert serverOptions != null;
        assertThat(serverOptions.getMap().get(UndertowOptions.SSL_USER_CIPHER_SUITES_ORDER)).isTrue();
    }

}
