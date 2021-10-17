package com.midorlo.k12.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unused")
class ApplicationPropertiesTest {

    private ApplicationProperties properties;

    @BeforeEach
    void setup() {
        properties = new ApplicationProperties();
    }

    /**
     * Invokes all getters reflectively, collecting their values.
     */
    private void reflect(Object obj, Set<String> dst, String prefix) throws Exception {
        Class<?> src = obj.getClass();
        for (Method method : src.getDeclaredMethods()) {
            String name = method.getName();
            if (name.startsWith("get")) {
                Object res = method.invoke(obj, (Object[]) null);
                if (res != null && src.equals(res.getClass().getDeclaringClass())) {
                    reflect(res, dst, prefix + name.substring(3));
                }
            } else if (name.startsWith("set")) {
                dst.add(prefix + name.substring(3));
            }
        }
    }

    @Test
    void testSecurityClientAuthorizationAccessTokenUri() {
        ApplicationProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val;
        assertThat(obj.getAccessTokenUri()).isEqualTo(null);
        val = "1" + null;
        obj.setAccessTokenUri(val);
        assertThat(obj.getAccessTokenUri()).isEqualTo(val);
    }

    @Test
    void testSecurityClientAuthorizationTokenServiceId() {
        ApplicationProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val;
        assertThat(obj.getTokenServiceId()).isEqualTo(null);
        val = "1" + null;
        obj.setTokenServiceId(val);
        assertThat(obj.getTokenServiceId()).isEqualTo(val);
    }

    @Test
    void testSecurityClientAuthorizationClientId() {
        ApplicationProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val;
        assertThat(obj.getClientId()).isEqualTo(null);
        val = "1" + null;
        obj.setClientId(val);
        assertThat(obj.getClientId()).isEqualTo(val);
    }

    @Test
    void testSecurityClientAuthorizationClientSecret() {
        ApplicationProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String val;
        assertThat(obj.getClientSecret()).isEqualTo(null);
        val = "1" + null;
        obj.setClientSecret(val);
        assertThat(obj.getClientSecret()).isEqualTo(val);
    }

    @Test
    void testSecurityAuthenticationJwtSecret() {
        ApplicationProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        String val;
        assertThat(obj.getSecret()).isEqualTo(null);
        val = "1" + null;
        obj.setSecret(val);
        assertThat(obj.getSecret()).isEqualTo(val);
    }

    @Test
    void testSecurityAuthenticationJwtBase64Secret() {
        ApplicationProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        String val;
        assertThat(obj.getSecret()).isEqualTo(null);
        val = "1" + null;
        obj.setBase64Secret(val);
        assertThat(obj.getBase64Secret()).isEqualTo(val);
    }

    @Test
    void testSecurityRememberMeKey() {
        ApplicationProperties.Security.RememberMe obj = properties.getSecurity().getRememberMe();
        String val;
        assertThat(obj.getKey()).isEqualTo(null);
        val = "1" + null;
        obj.setKey(val);
        assertThat(obj.getKey()).isEqualTo(val);
    }

    @Test
    void testSecurityOauth2Audience() {
        ApplicationProperties.Security.OAuth2 obj = properties.getSecurity().getOauth2();
        assertThat(obj).isNotNull();
        assertThat(obj.getAudience()).isNotNull().isEmpty();
        obj.setAudience(Arrays.asList("default", "account"));
        assertThat(obj.getAudience()).isNotEmpty().size().isEqualTo(2);
        assertThat(obj.getAudience()).contains("default", "account");
    }

    @Test
    void testApiDocsTermsOfServiceUrl() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String val;
        assertThat(obj.getTermsOfServiceUrl()).isEqualTo(null);
        val = "1" + null;
        obj.setTermsOfServiceUrl(val);
        assertThat(obj.getTermsOfServiceUrl()).isEqualTo(val);
    }

    @Test
    void testApiDocsContactName() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String val;
        assertThat(obj.getContactName()).isEqualTo(null);
        val = "1" + null;
        obj.setContactName(val);
        assertThat(obj.getContactName()).isEqualTo(val);
    }

    @Test
    void testApiDocsContactUrl() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String val;
        assertThat(obj.getContactUrl()).isEqualTo(null);
        val = "1" + null;
        obj.setContactUrl(val);
        assertThat(obj.getContactUrl()).isEqualTo(val);
    }

    @Test
    void testApiDocsContactEmail() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String val;
        assertThat(obj.getContactEmail()).isEqualTo(null);
        val = "1" + null;
        obj.setContactEmail(val);
        assertThat(obj.getContactEmail()).isEqualTo(val);
    }

    @Test
    void testApiDocsLicense() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String val;
        assertThat(obj.getLicense()).isEqualTo(null);
        val = "1" + null;
        obj.setLicense(val);
        assertThat(obj.getLicense()).isEqualTo(val);
    }

    @Test
    void testApiDocsLicenseUrl() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String val;
        assertThat(obj.getLicenseUrl()).isEqualTo(null);
        val = "1" + null;
        obj.setLicenseUrl(val);
        assertThat(obj.getLicenseUrl()).isEqualTo(val);
    }

    @Test
    void testApiDocsHost() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String val;
        assertThat(obj.getHost()).isEqualTo(null);
        val = "1" + null;
        obj.setHost(val);
        assertThat(obj.getHost()).isEqualTo(val);
    }

    @Test
    void testApiDocsServers() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        assertThat(obj.getServers()).isEmpty();
        ApplicationProperties.ApiDocs.Server server = new ApplicationProperties.ApiDocs.Server();
        server.setUrl("url");
        server.setDescription("description");
        server.setName("name");

        ApplicationProperties.ApiDocs.Server[] val = new ApplicationProperties.ApiDocs.Server[] { server };

        obj.setServers(val);
        assertThat(obj.getServers().length).isEqualTo(1);
        assertThat(obj.getServers()[0].getName()).isEqualTo(server.getName());
        assertThat(obj.getServers()[0].getUrl()).isEqualTo(server.getUrl());
        assertThat(obj.getServers()[0].getDescription()).isEqualTo(server.getDescription());
    }

    @Test
    void testRegistryPassword() {
        ApplicationProperties.Registry obj = properties.getRegistry();
        String val;
        assertThat(obj.getPassword()).isEqualTo(null);
        val = "1" + null;
        obj.setPassword(val);
        assertThat(obj.getPassword()).isEqualTo(val);
    }
}
