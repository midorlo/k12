package com.midorlo.k12.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Properties that are configurable through the application/(-env).yml file(s).
 * Also loads properties from git.properties and META-INF/build-actuator.properties (if found).
 */
@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@PropertySources({
        @PropertySource(value = "classpath:git.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:META-INF/build-actuator.properties", ignoreResourceNotFound = true),
})
public class ApplicationProperties {

    private final Meta    meta   = new Meta();
    private final License license  = new License();
    private final System  system = new System();

    private final Async             async       = new Async();
    private final Http              http        = new Http();
    private final Cache             cache       = new Cache();
    private final Mail              mail        = new Mail();
    private final Security          security    = new Security();
    private final ApiDocs           apiDocs     = new ApiDocs();
    private final Logging           logging     = new Logging();
    private final CorsConfiguration cors        = new CorsConfiguration();
    private final Social            social      = new Social();
    private final Gateway           gateway     = new Gateway();
    private final Registry          registry    = new Registry();
    private final ClientApp         clientApp   = new ClientApp();
    private final AuditEvents       auditEvents = new AuditEvents();

    @Data
    public static class System {
        private String  hostname;
        private Integer port;
    }

    @Data
    public static class Meta {
        private       String  name;
        private       String  title;
        private       String  description;
        private final Version version = new Version();

        @Data
        public static class Version {
            private Integer major;
            private Integer minor;
            private Integer bugfix;
            private String  tag;

            public String getVersion() {
                return major + "." + minor + "." + bugfix + "-" + tag;
            }
        }
    }

    @Data
    public static class License {
        private String name;
        private String url;
    }

    @Data
    public static class Async {
        private int corePoolSize  = 2;
        private int maxPoolSize   = 50;
        private int queueCapacity = 10000;
    }

    @Data
    public static class Http {
        private final Cache cache = new Cache();

        @Data
        public static class Cache {
            private int timeToLiveInDays = 1461;
        }
    }

    @Data
    public static class Cache {
        private final Ehcache ehcache = new Ehcache();

        @Data
        public static class Ehcache {
            private int  timeToLiveSeconds = 3600;
            private long maxEntries        = 100;
        }
    }

    @Data
    public static class Mail {
        private boolean enabled = false;
        private String  from    = "";
        private String  baseUrl = "";
    }

    @Data
    public static class Security {
        private final ClientAuthorization clientAuthorization   = new ClientAuthorization();
        private final Authentication      authentication        = new Authentication();
        private final RememberMe          rememberMe            = new RememberMe();
        private final OAuth2              oauth2                = new OAuth2();
        private       String              contentSecurityPolicy =
                "default-src 'self'; frame-src 'self' data:; script-src 'self' " +
                "'unsafe-inline' 'unsafe-eval' " +
                "https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; " +
                "img-src 'self' data:; font-src 'self' " +
                "data:";

        @Data
        public static class ClientAuthorization {
            private String accessTokenUri = null;
            private String tokenServiceId = null;
            private String clientId       = null;
            private String clientSecret   = null;
        }

        @Data
        public static class Authentication {
            private final Jwt jwt = new Jwt();

            @Data
            public static class Jwt {
                private String secret                              = null;
                private String base64Secret                        = null;
                private long   tokenValidityInSeconds              = 1800;
                private long   tokenValidityInSecondsForRememberMe = 2592000;
            }
        }

        @Data
        public static class RememberMe {
            @NotNull
            private String key = null;
        }

        @Data
        public static class OAuth2 {
            private List<String> audience = new ArrayList<>();
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
        private Server[] servers                    = {};

        @Data
        public static class Server {
            private String name;
            private String url;
            private String description;
        }
    }

    @Data
    public static class Logging {
        private final Logstash logstash      = new Logstash();
        private       boolean  useJsonFormat = false;

        @Data
        public static class Logstash {
            private boolean enabled   = false;
            private String  host      = "localhost";
            private int     port      = 5000;
            private int     queueSize = 512;
        }
    }

    @Data
    public static class Social {
        private String redirectAfterSignIn = "/#/home";
    }

    @Data
    public static class Gateway {
        private final RateLimiting              rateLimiting                     = new RateLimiting();
        private       Map<String, List<String>> authorizedMicroservicesEndpoints = new LinkedHashMap<>();

        @Data
        public static class RateLimiting {
            private boolean enabled           = false;
            private long    limit             = 100_000L;
            private int     durationInSeconds = 3_600;
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

    @Data
    public static class InitialContact {
        private String                      name;
        private String                      imageName;
        private String                      url;
        private String                      eMail;
        private String                      login;
        private String                      password;
        private Map<String, InitialContact> accounts = new LinkedHashMap<>();
    }

    @Data
    public static class InitialAccount {
        private String      login;
        private String      password;
        private InitialRole role;
    }

    @Data
    public static class InitialRole {
        private String name;
    }
}
