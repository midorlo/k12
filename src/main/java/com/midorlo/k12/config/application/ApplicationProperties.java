package com.midorlo.k12.config.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Properties specific to Application.
 *
 * <p> Properties are configured in the application.yml file. </p>
 * <p> This class also load properties in the Spring Environment from the git.properties and META-INF/build-info.properties
 * files if they are found in the classpath.</p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Async async = new Async();

    private final Http http = new Http();

    private final Cache cache = new Cache();

    private final Mail mail = new Mail();

    private final Security security = new Security();

    private final ApiDocs apiDocs = new ApiDocs();

    private final Logging logging = new Logging();

    private final CorsConfiguration cors = new CorsConfiguration();

    private final Social social = new Social();

    private final Gateway gateway = new Gateway();

    private final Registry registry = new Registry();

    private final ClientApp clientApp = new ClientApp();

    private final AuditEvents auditEvents = new AuditEvents();

    /**
     * <p>Getter for the field <code>async</code>.</p>
     *
     * @return a {@link Async} object.
     */
    public Async getAsync() {
        return async;
    }

    /**
     * <p>Getter for the field <code>http</code>.</p>
     *
     * @return a {@link Http} object.
     */
    public Http getHttp() {
        return http;
    }

    /**
     * <p>Getter for the field <code>cache</code>.</p>
     *
     * @return a {@link Cache} object.
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * <p>Getter for the field <code>mail</code>.</p>
     *
     * @return a {@link Mail} object.
     */
    public Mail getMail() {
        return mail;
    }

    /**
     * <p>Getter for the field <code>registry</code>.</p>
     *
     * @return a {@link Registry} object.
     */
    public Registry getRegistry() {
        return registry;
    }

    /**
     * <p>Getter for the field <code>security</code>.</p>
     *
     * @return a {@link Security} object.
     */
    public Security getSecurity() {
        return security;
    }

    /**
     * <p>Getter for the field <code>api-docs</code>.</p>
     *
     * @return a {@link ApiDocs} object.
     */
    public ApiDocs getApiDocs() {
        return apiDocs;
    }

    /**
     * <p>Getter for the field <code>logging</code>.</p>
     *
     * @return a {@link Logging} object.
     */
    public Logging getLogging() {
        return logging;
    }

    /**
     * <p>Getter for the field <code>cors</code>.</p>
     *
     * @return a {@link CorsConfiguration} object.
     */
    public CorsConfiguration getCors() {
        return cors;
    }

    /**
     * <p>Getter for the field <code>social</code>.</p>
     *
     * @return a {@link Social} object.
     */
    public Social getSocial() {
        return social;
    }

    /**
     * <p>Getter for the field <code>gateway</code>.</p>
     *
     * @return a {@link Gateway} object.
     */
    public Gateway getGateway() {
        return gateway;
    }

    /**
     * <p>Getter for the field <code>clientApp</code>.</p>
     *
     * @return a {@link ClientApp} object.
     */
    public ClientApp getClientApp() {
        return clientApp;
    }

    /**
     * <p>Getter for the field <code>auditEvents</code>.</p>
     *
     * @return a {@link AuditEvents} object.
     */
    public AuditEvents getAuditEvents() {
        return auditEvents;
    }

    public static class Async {

        private int corePoolSize = ApplicationDefaults.Async.corePoolSize;

        private int maxPoolSize = ApplicationDefaults.Async.maxPoolSize;

        private int queueCapacity = ApplicationDefaults.Async.queueCapacity;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    public static class Http {

        private final Cache cache = new Cache();

        public Cache getCache() {
            return cache;
        }

        public static class Cache {

            private int timeToLiveInDays = ApplicationDefaults.Http.Cache.timeToLiveInDays;

            public int getTimeToLiveInDays() {
                return timeToLiveInDays;
            }

            public void setTimeToLiveInDays(int timeToLiveInDays) {
                this.timeToLiveInDays = timeToLiveInDays;
            }
        }
    }

    public static class Cache {

        private final Ehcache ehcache = new Ehcache();

        public Ehcache getEhcache() {
            return ehcache;
        }

        public static class Ehcache {

            private int timeToLiveSeconds = ApplicationDefaults.Cache.Ehcache.timeToLiveSeconds;

            private long maxEntries = ApplicationDefaults.Cache.Ehcache.maxEntries;

            public int getTimeToLiveSeconds() {
                return timeToLiveSeconds;
            }

            public void setTimeToLiveSeconds(int timeToLiveSeconds) {
                this.timeToLiveSeconds = timeToLiveSeconds;
            }

            public long getMaxEntries() {
                return maxEntries;
            }

            public void setMaxEntries(long maxEntries) {
                this.maxEntries = maxEntries;
            }
        }
    }

    public static class Mail {

        private boolean enabled = ApplicationDefaults.Mail.enabled;

        private String from = ApplicationDefaults.Mail.from;

        private String baseUrl = ApplicationDefaults.Mail.baseUrl;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

    public static class Security {

        private String contentSecurityPolicy = ApplicationDefaults.Security.contentSecurityPolicy;

        private final ClientAuthorization clientAuthorization = new ClientAuthorization();

        private final Authentication authentication = new Authentication();

        private final RememberMe rememberMe = new RememberMe();

        private final OAuth2 oauth2 = new OAuth2();

        public ClientAuthorization getClientAuthorization() {
            return clientAuthorization;
        }

        public Authentication getAuthentication() {
            return authentication;
        }

        public RememberMe getRememberMe() {
            return rememberMe;
        }

        public OAuth2 getOauth2() {
            return oauth2;
        }

        public String getContentSecurityPolicy() {
            return contentSecurityPolicy;
        }

        public void setContentSecurityPolicy(String contentSecurityPolicy) {
            this.contentSecurityPolicy = contentSecurityPolicy;
        }

        public static class ClientAuthorization {

            private String accessTokenUri = ApplicationDefaults.Security.ClientAuthorization.accessTokenUri;

            private String tokenServiceId = ApplicationDefaults.Security.ClientAuthorization.tokenServiceId;

            private String clientId = ApplicationDefaults.Security.ClientAuthorization.clientId;

            private String clientSecret = ApplicationDefaults.Security.ClientAuthorization.clientSecret;

            public String getAccessTokenUri() {
                return accessTokenUri;
            }

            public void setAccessTokenUri(String accessTokenUri) {
                this.accessTokenUri = accessTokenUri;
            }

            public String getTokenServiceId() {
                return tokenServiceId;
            }

            public void setTokenServiceId(String tokenServiceId) {
                this.tokenServiceId = tokenServiceId;
            }

            public String getClientId() {
                return clientId;
            }

            public void setClientId(String clientId) {
                this.clientId = clientId;
            }

            public String getClientSecret() {
                return clientSecret;
            }

            public void setClientSecret(String clientSecret) {
                this.clientSecret = clientSecret;
            }
        }

        public static class Authentication {

            private final Jwt jwt = new Jwt();

            public Jwt getJwt() {
                return jwt;
            }

            public static class Jwt {

                private String secret = ApplicationDefaults.Security.Authentication.Jwt.secret;

                private String base64Secret = ApplicationDefaults.Security.Authentication.Jwt.base64Secret;

                private long tokenValidityInSeconds = ApplicationDefaults.Security.Authentication.Jwt
                    .tokenValidityInSeconds;

                private long tokenValidityInSecondsForRememberMe = ApplicationDefaults.Security.Authentication.Jwt
                    .tokenValidityInSecondsForRememberMe;

                public String getSecret() {
                    return secret;
                }

                public void setSecret(String secret) {
                    this.secret = secret;
                }

                public String getBase64Secret() {
                    return base64Secret;
                }

                public void setBase64Secret(String base64Secret) {
                    this.base64Secret = base64Secret;
                }

                public long getTokenValidityInSeconds() {
                    return tokenValidityInSeconds;
                }

                public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                    this.tokenValidityInSeconds = tokenValidityInSeconds;
                }

                public long getTokenValidityInSecondsForRememberMe() {
                    return tokenValidityInSecondsForRememberMe;
                }

                public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                    this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
                }
            }
        }

        public static class RememberMe {

            @NotNull
            private String key = ApplicationDefaults.Security.RememberMe.key;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }

        public static class OAuth2 {
            private final List<String> audience = new ArrayList<>();

            public List<String> getAudience() {
                return Collections.unmodifiableList(audience);
            }

            public void setAudience(@NotNull List<String> audience) {
                this.audience.addAll(audience);
            }
        }
    }

    public static class ApiDocs {

        private String title = ApplicationDefaults.ApiDocs.title;

        private String description = ApplicationDefaults.ApiDocs.description;

        private String version = ApplicationDefaults.ApiDocs.version;

        private String termsOfServiceUrl = ApplicationDefaults.ApiDocs.termsOfServiceUrl;

        private String contactName = ApplicationDefaults.ApiDocs.contactName;

        private String contactUrl = ApplicationDefaults.ApiDocs.contactUrl;

        private String contactEmail = ApplicationDefaults.ApiDocs.contactEmail;

        private String license = ApplicationDefaults.ApiDocs.license;

        private String licenseUrl = ApplicationDefaults.ApiDocs.licenseUrl;

        private String defaultIncludePattern = ApplicationDefaults.ApiDocs.defaultIncludePattern;

        private String managementIncludePattern = ApplicationDefaults.ApiDocs.managementIncludePattern;

        private String host = ApplicationDefaults.ApiDocs.host;

        private String[] protocols = ApplicationDefaults.ApiDocs.protocols;

        private Server[] servers = {};

        private boolean useDefaultResponseMessages = ApplicationDefaults.ApiDocs.useDefaultResponseMessages;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getTermsOfServiceUrl() {
            return termsOfServiceUrl;
        }

        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContactUrl() {
            return contactUrl;
        }

        public void setContactUrl(String contactUrl) {
            this.contactUrl = contactUrl;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }

        public String getDefaultIncludePattern() {
            return defaultIncludePattern;
        }

        public void setDefaultIncludePattern(String defaultIncludePattern) {
            this.defaultIncludePattern = defaultIncludePattern;
        }

        public String getManagementIncludePattern() {
            return managementIncludePattern;
        }

        public void setManagementIncludePattern(String managementIncludePattern) {
            this.managementIncludePattern = managementIncludePattern;
        }

        public String getHost() {
            return host;
        }

        public void setHost(final String host) {
            this.host = host;
        }

        public String[] getProtocols() {
            return protocols;
        }

        public void setProtocols(final String[] protocols) {
            this.protocols = protocols;
        }

        public Server[] getServers() {
            return servers;
        }

        public void setServers(final Server[] servers) {
            this.servers = servers;
        }

        public boolean isUseDefaultResponseMessages() {
            return useDefaultResponseMessages;
        }

        public void setUseDefaultResponseMessages(final boolean useDefaultResponseMessages) {
            this.useDefaultResponseMessages = useDefaultResponseMessages;
        }

        public static class Server {
            private String name;
            private String url;
            private String description;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
    }

    public static class Logging {

        private boolean useJsonFormat = ApplicationDefaults.Logging.useJsonFormat;

        private final Logstash logstash = new Logstash();

        public boolean isUseJsonFormat() {
            return useJsonFormat;
        }

        public void setUseJsonFormat(boolean useJsonFormat) {
            this.useJsonFormat = useJsonFormat;
        }

        public Logstash getLogstash() {
            return logstash;
        }

        public static class Logstash {

            private boolean enabled = ApplicationDefaults.Logging.Logstash.enabled;

            private String host = ApplicationDefaults.Logging.Logstash.host;

            private int port = ApplicationDefaults.Logging.Logstash.port;

            private int queueSize = ApplicationDefaults.Logging.Logstash.queueSize;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public int getQueueSize() {
                return queueSize;
            }

            public void setQueueSize(int queueSize) {
                this.queueSize = queueSize;
            }
        }
    }

    public static class Social {

        private String redirectAfterSignIn = ApplicationDefaults.Social.redirectAfterSignIn;

        public String getRedirectAfterSignIn() {
            return redirectAfterSignIn;
        }

        public void setRedirectAfterSignIn(String redirectAfterSignIn) {
            this.redirectAfterSignIn = redirectAfterSignIn;
        }
    }

    public static class Gateway {

        private final RateLimiting rateLimiting = new RateLimiting();

        public RateLimiting getRateLimiting() {
            return rateLimiting;
        }

        private Map<String, List<String>> authorizedMicroservicesEndpoints = ApplicationDefaults.Gateway
            .authorizedMicroservicesEndpoints;

        public Map<String, List<String>> getAuthorizedMicroservicesEndpoints() {
            return authorizedMicroservicesEndpoints;
        }

        public void setAuthorizedMicroservicesEndpoints(Map<String, List<String>> authorizedMicroservicesEndpoints) {
            this.authorizedMicroservicesEndpoints = authorizedMicroservicesEndpoints;
        }

        public static class RateLimiting {

            private boolean enabled = ApplicationDefaults.Gateway.RateLimiting.enabled;

            private long limit = ApplicationDefaults.Gateway.RateLimiting.limit;

            private int durationInSeconds = ApplicationDefaults.Gateway.RateLimiting.durationInSeconds;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public long getLimit() {
                return this.limit;
            }

            public void setLimit(long limit) {
                this.limit = limit;
            }

            public int getDurationInSeconds() {
                return durationInSeconds;
            }

            public void setDurationInSeconds(int durationInSeconds) {
                this.durationInSeconds = durationInSeconds;
            }
        }
    }

    public static class Registry {

        private String password = ApplicationDefaults.Registry.password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class ClientApp {

        private String name = ApplicationDefaults.ClientApp.name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class AuditEvents {
        private int retentionPeriod = ApplicationDefaults.AuditEvents.retentionPeriod;

        public int getRetentionPeriod() {
            return retentionPeriod;
        }

        public void setRetentionPeriod(int retentionPeriod) {
            this.retentionPeriod = retentionPeriod;
        }
    }
}
