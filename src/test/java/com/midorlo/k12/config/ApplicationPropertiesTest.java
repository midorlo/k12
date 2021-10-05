package com.midorlo.k12.config;

import com.midorlo.k12.config.application.ApplicationDefaults;
import com.midorlo.k12.config.application.ApplicationProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationPropertiesTest {

    private ApplicationProperties properties;

    @BeforeEach
    void setup() {
        properties = new ApplicationProperties();
    }

    @Test
    void testComplete() throws Exception {
        // Slightly pedantic; this checks if there are tests for each of the properties.
        Set<String> set = new LinkedHashSet<>(64, 1F);
        reflect(properties, set, "test");
        for (String name : set) {
            assertThat(this.getClass().getDeclaredMethod(name)).isNotNull();
        }
    }

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
    void testAsyncCorePoolSize() {
        ApplicationProperties.Async obj = properties.getAsync();
        int                         val = ApplicationDefaults.Async.corePoolSize;
        assertThat(obj.getCorePoolSize()).isEqualTo(val);
        val++;
        obj.setCorePoolSize(val);
        assertThat(obj.getCorePoolSize()).isEqualTo(val);
    }

    @Test
    void testAsyncMaxPoolSize() {
        ApplicationProperties.Async obj = properties.getAsync();
        int                         val = ApplicationDefaults.Async.maxPoolSize;
        assertThat(obj.getMaxPoolSize()).isEqualTo(val);
        val++;
        obj.setMaxPoolSize(val);
        assertThat(obj.getMaxPoolSize()).isEqualTo(val);
    }

    @Test
    void testAsyncQueueCapacity() {
        ApplicationProperties.Async obj = properties.getAsync();
        int                         val = ApplicationDefaults.Async.queueCapacity;
        assertThat(obj.getQueueCapacity()).isEqualTo(val);
        val++;
        obj.setQueueCapacity(val);
        assertThat(obj.getQueueCapacity()).isEqualTo(val);
    }

    @Test
    void testHttpCacheTimeToLiveInDays() {
        ApplicationProperties.Http.Cache obj = properties.getHttp().getCache();
        int                              val = ApplicationDefaults.Http.Cache.timeToLiveInDays;
        assertThat(obj.getTimeToLiveInDays()).isEqualTo(val);
        val++;
        obj.setTimeToLiveInDays(val);
        assertThat(obj.getTimeToLiveInDays()).isEqualTo(val);
    }

    @Test
    void testDatabaseCouchbaseBucketName() {
        ApplicationProperties.Database.Couchbase obj = properties.getDatabase().getCouchbase();
        assertThat(obj.getBucketName()).isEqualTo(null);
        obj.setBucketName("bucketName");
        assertThat(obj.getBucketName()).isEqualTo("bucketName");
    }

    @Test
    void testCacheHazelcastTimeToLiveSeconds() {
        ApplicationProperties.Cache.Hazelcast obj = properties.getCache().getHazelcast();
        int                                   val = ApplicationDefaults.Cache.Hazelcast.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    void testCacheHazelcastBackupCount() {
        ApplicationProperties.Cache.Hazelcast obj = properties.getCache().getHazelcast();
        int                                   val = ApplicationDefaults.Cache.Hazelcast.backupCount;
        assertThat(obj.getBackupCount()).isEqualTo(val);
        val++;
        obj.setBackupCount(val);
        assertThat(obj.getBackupCount()).isEqualTo(val);
    }

    @Test
    void testCacheCaffeineTimeToLiveSeconds() {
        ApplicationProperties.Cache.Caffeine obj = properties.getCache().getCaffeine();
        int                                  val = ApplicationDefaults.Cache.Caffeine.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    void testCacheCaffeineMaxEntries() {
        ApplicationProperties.Cache.Caffeine obj = properties.getCache().getCaffeine();
        long                                 val = ApplicationDefaults.Cache.Caffeine.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    void testCacheEhcacheTimeToLiveSeconds() {
        ApplicationProperties.Cache.Ehcache obj = properties.getCache().getEhcache();
        int                                 val = ApplicationDefaults.Cache.Ehcache.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    void testCacheEhcacheMaxEntries() {
        ApplicationProperties.Cache.Ehcache obj = properties.getCache().getEhcache();
        long                                val = ApplicationDefaults.Cache.Ehcache.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    void testCacheInfinispanConfigFile() {
        ApplicationProperties.Cache.Infinispan obj = properties.getCache().getInfinispan();
        String                                 val = ApplicationDefaults.Cache.Infinispan.configFile;
        assertThat(obj.getConfigFile()).isEqualTo(val);
        val = "1" + val;
        obj.setConfigFile(val);
        assertThat(obj.getConfigFile()).isEqualTo(val);
    }

    @Test
    void testCacheInfinispanStatsEnabled() {
        ApplicationProperties.Cache.Infinispan obj = properties.getCache().getInfinispan();
        boolean                                val = ApplicationDefaults.Cache.Infinispan.statsEnabled;
        assertThat(obj.isStatsEnabled()).isEqualTo(val);
        val = !val;
        obj.setStatsEnabled(val);
        assertThat(obj.isStatsEnabled()).isEqualTo(val);
    }

    @Test
    void testCacheInfinispanLocalTimeToLiveSeconds() {
        ApplicationProperties.Cache.Infinispan.Local obj = properties.getCache().getInfinispan().getLocal();
        long                                         val = ApplicationDefaults.Cache.Infinispan.Local.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    void testCacheInfinispanLocalMaxEntries() {
        ApplicationProperties.Cache.Infinispan.Local obj = properties.getCache().getInfinispan().getLocal();
        long                                         val = ApplicationDefaults.Cache.Infinispan.Local.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    void testCacheInfinispanDistributedTimeToLiveSeconds() {
        ApplicationProperties.Cache.Infinispan.Distributed obj = properties.getCache().getInfinispan().getDistributed();
        long val =
            ApplicationDefaults.Cache.Infinispan.Distributed.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    void testCacheInfinispanDistributedMaxEntries() {
        ApplicationProperties.Cache.Infinispan.Distributed obj = properties.getCache().getInfinispan().getDistributed();
        long val =
            ApplicationDefaults.Cache.Infinispan.Distributed.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    void testCacheInfinispanDistributedInstanceCount() {
        ApplicationProperties.Cache.Infinispan.Distributed obj = properties.getCache().getInfinispan().getDistributed();
        int val =
            ApplicationDefaults.Cache.Infinispan.Distributed.instanceCount;
        assertThat(obj.getInstanceCount()).isEqualTo(val);
        val++;
        obj.setInstanceCount(val);
        assertThat(obj.getInstanceCount()).isEqualTo(val);
    }

    @Test
    void testCacheInfinispanReplicatedTimeToLiveSeconds() {
        ApplicationProperties.Cache.Infinispan.Replicated obj = properties.getCache().getInfinispan().getReplicated();
        long val =
            ApplicationDefaults.Cache.Infinispan.Replicated.timeToLiveSeconds;
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
        val++;
        obj.setTimeToLiveSeconds(val);
        assertThat(obj.getTimeToLiveSeconds()).isEqualTo(val);
    }

    @Test
    void testCacheInfinispanReplicatedMaxEntries() {
        ApplicationProperties.Cache.Infinispan.Replicated obj = properties.getCache().getInfinispan().getReplicated();
        long val =
            ApplicationDefaults.Cache.Infinispan.Replicated.maxEntries;
        assertThat(obj.getMaxEntries()).isEqualTo(val);
        val++;
        obj.setMaxEntries(val);
        assertThat(obj.getMaxEntries()).isEqualTo(val);
    }

    @Test
    void testCacheMemcachedEnabled() {
        ApplicationProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        boolean                               val = ApplicationDefaults.Cache.Memcached.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        obj.setEnabled(true);
        assertThat(obj.isEnabled()).isEqualTo(true);
    }

    @Test
    void testCacheMemcachedServers() {
        ApplicationProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        String                                val = ApplicationDefaults.Cache.Memcached.servers;
        assertThat(obj.getServers()).isEqualTo(val);
        val = "myserver:1337";
        obj.setServers(val);
        assertThat(obj.getServers()).isEqualTo(val);
    }

    @Test
    void testCacheMemcachedExpiration() {
        ApplicationProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        int                                   val = ApplicationDefaults.Cache.Memcached.expiration;
        assertThat(obj.getExpiration()).isEqualTo(val);
        val++;
        obj.setExpiration(val);
        assertThat(obj.getExpiration()).isEqualTo(val);
    }

    @Test
    void testCacheMemcachedUseBinaryProtocol() {
        ApplicationProperties.Cache.Memcached obj = properties.getCache().getMemcached();
        boolean                               val = ApplicationDefaults.Cache.Memcached.useBinaryProtocol;
        assertThat(obj.isUseBinaryProtocol()).isEqualTo(val);
        obj.setUseBinaryProtocol(false);
        assertThat(obj.isUseBinaryProtocol()).isEqualTo(false);
    }

    @Test
    void testCacheMemcachedAuthenticationEnabled() {
        ApplicationProperties.Cache.Memcached.Authentication obj = properties.getCache().getMemcached()
                                                                             .getAuthentication();
        boolean val =
            ApplicationDefaults.Cache.Memcached.Authentication.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        obj.setEnabled(false);
        assertThat(obj.isEnabled()).isEqualTo(false);
    }

    @Test
    void testCacheMemcachedAuthenticationPassword() {
        ApplicationProperties.Cache.Memcached.Authentication obj = properties.getCache().getMemcached()
                                                                             .getAuthentication();
        assertThat(obj.getPassword()).isEqualTo(null);
        obj.setPassword("MEMCACHEPASSWORD");
        assertThat(obj.getPassword()).isEqualTo("MEMCACHEPASSWORD");
    }

    @Test
    void testCacheMemcachedAuthenticationUsername() {
        ApplicationProperties.Cache.Memcached.Authentication obj = properties.getCache().getMemcached()
                                                                             .getAuthentication();
        assertThat(obj.getUsername()).isEqualTo(null);
        obj.setUsername("MEMCACHEUSER");
        assertThat(obj.getUsername()).isEqualTo("MEMCACHEUSER");
    }

    @Test
    void testMailFrom() {
        ApplicationProperties.Mail obj = properties.getMail();
        String                     val = ApplicationDefaults.Mail.from;
        assertThat(obj.getFrom()).isEqualTo(val);
        val = "1" + val;
        obj.setFrom(val);
        assertThat(obj.getFrom()).isEqualTo(val);
    }

    @Test
    void testMailBaseUrl() {
        ApplicationProperties.Mail obj = properties.getMail();
        String                     val = ApplicationDefaults.Mail.baseUrl;
        assertThat(obj.getBaseUrl()).isEqualTo(val);
        val = "1" + val;
        obj.setBaseUrl(val);
        assertThat(obj.getBaseUrl()).isEqualTo(val);
    }

    @Test
    void testMailEnabled() {
        ApplicationProperties.Mail obj = properties.getMail();
        boolean                    val = ApplicationDefaults.Mail.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    void testSecurityClientAuthorizationAccessTokenUri() {
        ApplicationProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String                                             val;
        assertThat(obj.getAccessTokenUri()).isEqualTo(null);
        val = "1" + null;
        obj.setAccessTokenUri(val);
        assertThat(obj.getAccessTokenUri()).isEqualTo(val);
    }

    @Test
    void testSecurityClientAuthorizationTokenServiceId() {
        ApplicationProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String                                             val;
        assertThat(obj.getTokenServiceId()).isEqualTo(null);
        val = "1" + null;
        obj.setTokenServiceId(val);
        assertThat(obj.getTokenServiceId()).isEqualTo(val);
    }

    @Test
    void testSecurityClientAuthorizationClientId() {
        ApplicationProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String                                             val;
        assertThat(obj.getClientId()).isEqualTo(null);
        val = "1" + null;
        obj.setClientId(val);
        assertThat(obj.getClientId()).isEqualTo(val);
    }

    @Test
    void testSecurityClientAuthorizationClientSecret() {
        ApplicationProperties.Security.ClientAuthorization obj = properties.getSecurity().getClientAuthorization();
        String                                             val;
        assertThat(obj.getClientSecret()).isEqualTo(null);
        val = "1" + null;
        obj.setClientSecret(val);
        assertThat(obj.getClientSecret()).isEqualTo(val);
    }

    @Test
    void testSecurityAuthenticationJwtSecret() {
        ApplicationProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        String                                            val;
        assertThat(obj.getSecret()).isEqualTo(null);
        val = "1" + null;
        obj.setSecret(val);
        assertThat(obj.getSecret()).isEqualTo(val);
    }

    @Test
    void testSecurityAuthenticationJwtBase64Secret() {
        ApplicationProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        String                                            val;
        assertThat(obj.getSecret()).isEqualTo(null);
        val = "1" + null;
        obj.setBase64Secret(val);
        assertThat(obj.getBase64Secret()).isEqualTo(val);
    }

    @Test
    void testSecurityAuthenticationJwtTokenValidityInSeconds() {
        ApplicationProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        long val =
            ApplicationDefaults.Security.Authentication.Jwt.tokenValidityInSeconds;
        assertThat(obj.getTokenValidityInSeconds()).isEqualTo(val);
        val++;
        obj.setTokenValidityInSeconds(val);
        assertThat(obj.getTokenValidityInSeconds()).isEqualTo(val);
    }

    @Test
    void testSecurityAuthenticationJwtTokenValidityInSecondsForRememberMe() {
        ApplicationProperties.Security.Authentication.Jwt obj = properties.getSecurity().getAuthentication().getJwt();
        long val =
            ApplicationDefaults.Security.Authentication.Jwt.tokenValidityInSecondsForRememberMe;
        assertThat(obj.getTokenValidityInSecondsForRememberMe()).isEqualTo(val);
        val++;
        obj.setTokenValidityInSecondsForRememberMe(val);
        assertThat(obj.getTokenValidityInSecondsForRememberMe()).isEqualTo(val);
    }

    @Test
    void testSecurityRememberMeKey() {
        ApplicationProperties.Security.RememberMe obj = properties.getSecurity().getRememberMe();
        String                                    val;
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
    void testApiDocsTitle() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val = ApplicationDefaults.ApiDocs.title;
        assertThat(obj.getTitle()).isEqualTo(val);
        val = "1" + val;
        obj.setTitle(val);
        assertThat(obj.getTitle()).isEqualTo(val);
    }

    @Test
    void testApiDocsDescription() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val = ApplicationDefaults.ApiDocs.description;
        assertThat(obj.getDescription()).isEqualTo(val);
        val = "1" + val;
        obj.setDescription(val);
        assertThat(obj.getDescription()).isEqualTo(val);
    }

    @Test
    void testApiDocsVersion() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val = ApplicationDefaults.ApiDocs.version;
        assertThat(obj.getVersion()).isEqualTo(val);
        val = "1" + val;
        obj.setVersion(val);
        assertThat(obj.getVersion()).isEqualTo(val);
    }

    @Test
    void testApiDocsTermsOfServiceUrl() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val;
        assertThat(obj.getTermsOfServiceUrl()).isEqualTo(null);
        val = "1" + null;
        obj.setTermsOfServiceUrl(val);
        assertThat(obj.getTermsOfServiceUrl()).isEqualTo(val);
    }

    @Test
    void testApiDocsContactName() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val;
        assertThat(obj.getContactName()).isEqualTo(null);
        val = "1" + null;
        obj.setContactName(val);
        assertThat(obj.getContactName()).isEqualTo(val);
    }

    @Test
    void testApiDocsContactUrl() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val;
        assertThat(obj.getContactUrl()).isEqualTo(null);
        val = "1" + null;
        obj.setContactUrl(val);
        assertThat(obj.getContactUrl()).isEqualTo(val);
    }

    @Test
    void testApiDocsContactEmail() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val;
        assertThat(obj.getContactEmail()).isEqualTo(null);
        val = "1" + null;
        obj.setContactEmail(val);
        assertThat(obj.getContactEmail()).isEqualTo(val);
    }

    @Test
    void testApiDocsLicense() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val;
        assertThat(obj.getLicense()).isEqualTo(null);
        val = "1" + null;
        obj.setLicense(val);
        assertThat(obj.getLicense()).isEqualTo(val);
    }

    @Test
    void testApiDocsLicenseUrl() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val;
        assertThat(obj.getLicenseUrl()).isEqualTo(null);
        val = "1" + null;
        obj.setLicenseUrl(val);
        assertThat(obj.getLicenseUrl()).isEqualTo(val);
    }

    @Test
    void testApiDocsDefaultIncludePattern() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val = ApplicationDefaults.ApiDocs.defaultIncludePattern;
        assertThat(obj.getDefaultIncludePattern()).isEqualTo(val);
        val = "1" + val;
        obj.setDefaultIncludePattern(val);
        assertThat(obj.getDefaultIncludePattern()).isEqualTo(val);
    }

    @Test
    void testApiDocsManagementIncludePattern() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val = ApplicationDefaults.ApiDocs.managementIncludePattern;
        assertThat(obj.getManagementIncludePattern()).isEqualTo(val);
        val = "1" + val;
        obj.setManagementIncludePattern(val);
        assertThat(obj.getManagementIncludePattern()).isEqualTo(val);
    }

    @Test
    void testApiDocsHost() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String                        val;
        assertThat(obj.getHost()).isEqualTo(null);
        val = "1" + null;
        obj.setHost(val);
        assertThat(obj.getHost()).isEqualTo(val);
    }

    @Test
    void testApiDocsProtocols() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        String[]                      def = ApplicationDefaults.ApiDocs.protocols;
        List<String>                  val = new ArrayList<>(Arrays.asList(def));
        assertThat(obj.getProtocols()).containsExactlyElementsOf(val);
        val.add("1");
        obj.setProtocols(val.toArray(def));
        assertThat(obj.getProtocols()).containsExactlyElementsOf(val);
    }

    @Test
    void testApiDocsServers() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        assertThat(obj.getServers()).isEmpty();
        ApplicationProperties.ApiDocs.Server server = new ApplicationProperties.ApiDocs.Server();
        server.setUrl("url");
        server.setDescription("description");
        server.setName("name");

        ApplicationProperties.ApiDocs.Server[] val = new ApplicationProperties.ApiDocs.Server[]{ server };

        obj.setServers(val);
        assertThat(obj.getServers().length).isEqualTo(1);
        assertThat(obj.getServers()[0].getName()).isEqualTo(server.getName());
        assertThat(obj.getServers()[0].getUrl()).isEqualTo(server.getUrl());
        assertThat(obj.getServers()[0].getDescription()).isEqualTo(server.getDescription());
    }

    @Test
    void testApiDocsUseDefaultResponseMessages() {
        ApplicationProperties.ApiDocs obj = properties.getApiDocs();
        boolean                       val = ApplicationDefaults.ApiDocs.useDefaultResponseMessages;
        assertThat(obj.isUseDefaultResponseMessages()).isEqualTo(val);
        obj.setUseDefaultResponseMessages(false);
        assertThat(obj.isUseDefaultResponseMessages()).isEqualTo(false);
    }

    @Test
    void testLoggingUseJsonFormat() {
        ApplicationProperties.Logging obj = properties.getLogging();
        boolean                       val = ApplicationDefaults.Logging.useJsonFormat;
        assertThat(obj.isUseJsonFormat()).isEqualTo(val);
        obj.setUseJsonFormat(true);
        assertThat(obj.isUseJsonFormat()).isEqualTo(true);
    }

    @Test
    void testLoggingLogstashEnabled() {
        ApplicationProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        boolean                                val = ApplicationDefaults.Logging.Logstash.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    void testLoggingLogstashHost() {
        ApplicationProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        String                                 val = ApplicationDefaults.Logging.Logstash.host;
        assertThat(obj.getHost()).isEqualTo(val);
        val = "1" + val;
        obj.setHost(val);
        assertThat(obj.getHost()).isEqualTo(val);
    }

    @Test
    void testLoggingLogstashPort() {
        ApplicationProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        int                                    val = ApplicationDefaults.Logging.Logstash.port;
        assertThat(obj.getPort()).isEqualTo(val);
        val++;
        obj.setPort(val);
        assertThat(obj.getPort()).isEqualTo(val);
    }

    @Test
    void testLoggingLogstashQueueSize() {
        ApplicationProperties.Logging.Logstash obj = properties.getLogging().getLogstash();
        int                                    val = ApplicationDefaults.Logging.Logstash.queueSize;
        assertThat(obj.getQueueSize()).isEqualTo(val);
        val++;
        obj.setQueueSize(val);
        assertThat(obj.getQueueSize()).isEqualTo(val);
    }

    @Test
    void testSocialRedirectAfterSignIn() {
        ApplicationProperties.Social obj = properties.getSocial();
        String                       val = ApplicationDefaults.Social.redirectAfterSignIn;
        assertThat(obj.getRedirectAfterSignIn()).isEqualTo(val);
        val = "1" + val;
        obj.setRedirectAfterSignIn(val);
        assertThat(obj.getRedirectAfterSignIn()).isEqualTo(val);
    }

    @Test
    void testGatewayAuthorizedMicroservicesEndpoints() {
        ApplicationProperties.Gateway obj = properties.getGateway();
        Map<String, List<String>>     val = ApplicationDefaults.Gateway.authorizedMicroservicesEndpoints;
        assertThat(obj.getAuthorizedMicroservicesEndpoints()).isEqualTo(val);
        val.put("1", null);
        obj.setAuthorizedMicroservicesEndpoints(val);
        assertThat(obj.getAuthorizedMicroservicesEndpoints()).isEqualTo(val);
    }

    @Test
    void testGatewayRateLimitingEnabled() {
        ApplicationProperties.Gateway.RateLimiting obj = properties.getGateway().getRateLimiting();
        boolean                                    val = ApplicationDefaults.Gateway.RateLimiting.enabled;
        assertThat(obj.isEnabled()).isEqualTo(val);
        val = !val;
        obj.setEnabled(val);
        assertThat(obj.isEnabled()).isEqualTo(val);
    }

    @Test
    void testGatewayRateLimitingLimit() {
        ApplicationProperties.Gateway.RateLimiting obj = properties.getGateway().getRateLimiting();
        long                                       val = ApplicationDefaults.Gateway.RateLimiting.limit;
        assertThat(obj.getLimit()).isEqualTo(val);
        val++;
        obj.setLimit(val);
        assertThat(obj.getLimit()).isEqualTo(val);
    }

    @Test
    void testGatewayRateLimitingDurationInSeconds() {
        ApplicationProperties.Gateway.RateLimiting obj = properties.getGateway().getRateLimiting();
        int                                        val = ApplicationDefaults.Gateway.RateLimiting.durationInSeconds;
        assertThat(obj.getDurationInSeconds()).isEqualTo(val);
        val++;
        obj.setDurationInSeconds(val);
        assertThat(obj.getDurationInSeconds()).isEqualTo(val);
    }

    @Test
    void testRegistryPassword() {
        ApplicationProperties.Registry obj = properties.getRegistry();
        String                         val;
        assertThat(obj.getPassword()).isEqualTo(null);
        val = "1" + null;
        obj.setPassword(val);
        assertThat(obj.getPassword()).isEqualTo(val);
    }

    @Test
    void testClientAppName() {
        ApplicationProperties.ClientApp obj = properties.getClientApp();
        String                          val = ApplicationDefaults.ClientApp.name;
        assertThat(obj.getName()).isEqualTo(val);
        val = "1" + val;
        obj.setName(val);
        assertThat(obj.getName()).isEqualTo(val);
    }

    @Test
    void testAuditEventsRetentionPeriod() {
        ApplicationProperties.AuditEvents obj = properties.getAuditEvents();
        int                               val = ApplicationDefaults.AuditEvents.retentionPeriod;
        assertThat(obj.getRetentionPeriod()).isEqualTo(val);
        val++;
        obj.setRetentionPeriod(val);
        assertThat(obj.getRetentionPeriod()).isEqualTo(val);
    }

    @Test
    void testSecurityContentSecurityPolicy() {
        ApplicationProperties.Security obj = properties.getSecurity();
        String                         val = ApplicationDefaults.Security.contentSecurityPolicy;
        assertThat(obj.getContentSecurityPolicy()).isEqualTo(val);
        obj.setContentSecurityPolicy("foobar");
        assertThat(obj.getContentSecurityPolicy()).isEqualTo("foobar");
    }
}
