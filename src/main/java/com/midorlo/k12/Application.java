package com.midorlo.k12;

import com.midorlo.k12.configuration.ApplicationConstants;
import com.midorlo.k12.configuration.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static com.midorlo.k12.configuration.ApplicationConstants.ContextConstants.*;
import static com.midorlo.k12.configuration.ApplicationConstants.REPOSITORY_PACKAGE;
import static com.midorlo.k12.configuration.ApplicationConstants.TechConstants.AUDITOR_BEAN;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories(REPOSITORY_PACKAGE)
@EnableJpaAuditing(auditorAwareRef = AUDITOR_BEAN)
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.midorlo.k12", "springfox.documentation.schema" })
@EnableConfigurationProperties({ ApplicationProperties.class })
public class Application {

    private final Environment env;

    public Application(Environment env) {
        this.env = env;
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https")
                                  .orElse("http");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional
            .ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            "\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}{}\n\t" +
            "External: \t{}://{}:{}{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("application.meta.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }

    /**
     * Set a default to use when no profile is configured.
     *
     * @param app the Spring application.
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap<>();
        /*
         * The default profile to use when no other profiles are defined
         * This cannot be set in the application.yml file.
         * See https://github.com/spring-projects/spring-boot/issues/1219
         */
        defProperties.put(ApplicationConstants.SPRING_PROFILE_DEFAULT, SPRING_PROFILE_DEVELOPMENT);
        app.setDefaultProperties(defProperties);
    }

    /**
     * Verifies that no conflicting application contexts are loaded.
     */
    @PostConstruct
    public void verifyContexts() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        if (
            profiles.containsAll(Set.of(SPRING_PROFILE_DEVELOPMENT, SPRING_PROFILE_PRODUCTION)) ||
            profiles.containsAll(Set.of(SPRING_PROFILE_DEVELOPMENT, SPRING_PROFILE_CLOUD))
        ) {
            log.error("Configuration error! Conflicting profiles are active.");
        }
    }
}
