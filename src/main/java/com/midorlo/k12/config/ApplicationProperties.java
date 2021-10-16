package com.midorlo.k12.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Properties specific to Application.
 *
 * <p> Properties are configured in the application.yml file. </p>
 * <p> This class also load properties in the Spring Environment from the git.properties and META-INF/build-info
 * .properties
 * files if they are found in the classpath.</p>
 */
@Data
@EnableConfigurationProperties(ApplicationProperties.class)
// Load some properties into the environment from files to make them available for interpolation in application.yaml.
@PropertySources(
    {
        @PropertySource(value = "classpath:git.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:META-INF/build-info.properties", ignoreResourceNotFound = true),
    }
)
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

    @Data
    public static class Async {

        private int corePoolSize = 2;

        private int maxPoolSize = 50;

        private int queueCapacity = 10000;

    }

    @Data
    public static class Http {

        private final Cache cache = new Cache();

        public static class Cache {

            private int timeToLiveInDays = 1461;

            public int getTimeToLiveInDays() {
                return timeToLiveInDays;
            }

            public void setTimeToLiveInDays(int timeToLiveInDays) {
                this.timeToLiveInDays = timeToLiveInDays;
            }
        }
    }

    @Data
    public static class Cache {

        private final Ehcache ehcache = new Ehcache();

        public static class Ehcache {

            private int timeToLiveSeconds = 3600;

            private long maxEntries = 100;

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

    @Data
    public static class Mail {

        private boolean enabled = false;

        private String from = "";

        private String baseUrl = "";

    }

    @Data
    public static class Security {

        private String contentSecurityPolicy = "default-src 'self'; frame-src 'self' data:; script-src 'self' " +
                                               "'unsafe-inline' 'unsafe-eval' " +
                                               "https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; " +
                                               "img-src 'self' data:; font-src 'self' " +
                                               "data:";

        private final ClientAuthorization clientAuthorization = new ClientAuthorization();

        private final Authentication authentication = new Authentication();

        private final RememberMe rememberMe = new RememberMe();

        private final OAuth2 oauth2 = new OAuth2();

        public static class ClientAuthorization {

            private String accessTokenUri = null;

            private String tokenServiceId = null;

            private String clientId = null;

            private String clientSecret = null;

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

                private String secret = null;

                private String base64Secret = null;

                private long tokenValidityInSeconds = 1800;

                private long tokenValidityInSecondsForRememberMe = 2592000;

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
            private String key = null;

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

    @Data
    public static class ApiDocs {

        private String   title                      = "Application API";
        private String   description                = "API documentation";
        private String   version                    = "0.0.1";
        private String   termsOfServiceUrl          = null;
        private String   contactName                = null;
        private String   contactUrl                 = null;
        private String   contactEmail               = null;
        private String   license                    = null;
        private String   licenseUrl                 = null;
        private String   defaultIncludePattern      = "/api/.*";
        private String   managementIncludePattern   = "/management/.*";
        private String   host                       = null;
        private String[] protocols                  = {};
        private boolean  useDefaultResponseMessages = true;

        private Server[] servers = {};

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

    @Data
    public static class Logging {

        private boolean useJsonFormat = false;

        private final Logstash logstash = new Logstash();

        public static class Logstash {

            private boolean enabled   = false;
            private String  host      = "localhost";
            private int     port      = 5000;
            private int     queueSize = 512;

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

    @Data
    public static class Social {

        private String redirectAfterSignIn = "/#/home";

    }

    @Data
    public static class Gateway {

        private final RateLimiting rateLimiting = new RateLimiting();

        private Map<String, List<String>> authorizedMicroservicesEndpoints = new LinkedHashMap<>();

        public static class RateLimiting {

            private boolean enabled           = false;
            private long    limit             = 100_000L;
            private int     durationInSeconds = 3_600;

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

    @Data
    public static class Registry {

        private String password = null;

    }

    @Data
    public static class ClientApp {

        private String name = "applicationApp";

    }

    @Data
    public static class AuditEvents {

        private int retentionPeriod = 30;

    }
}
