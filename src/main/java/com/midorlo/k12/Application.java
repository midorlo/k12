package com.midorlo.k12;

import com.midorlo.k12.config.ApplicationConstants;
import com.midorlo.k12.config.ApplicationProperties;
import com.midorlo.k12.config.util.DefaultProfileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Application main Class.
 */

@Slf4j
@Data
@SpringBootApplication
@EnableConfigurationProperties({
    LiquibaseProperties.class,
    ApplicationProperties.class
})
public class Application {

    private final Environment env;

    public Application(Environment env) {
        this.env = env;
    }

    /**
     * Application main entry point.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(Application.class);
        DefaultProfileUtil.addDefaultProfile(app);

        ConfigurableApplicationContext applicationContext = app.run(args);

        Environment env = applicationContext.getEnvironment();
        logApplicationStartup(env);
    }

    /**
     * <p>Verifies that no conflicting profiles are active.</p>
     */
    @PostConstruct
    public void verifyEnabledProfiles() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("Configuration Error: Both 'dev' and 'prod' profiles are enabled.");
        }
        if (activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(ApplicationConstants.SPRING_PROFILE_CLOUD)) {
            log.error("Configuration Error: Both 'dev' and 'cloud' profiles are enabled.");
        }
    }

    private static void logApplicationStartup(Environment env) {
        String protocol    = getProtocol(env);
        String serverPort  = env.getProperty("server.port");
        String contextPath = getContextPath(env);
        String hostAddress = getHostAddress();
        log.info("\n" +
                 "----------------------------------------------------------" +
                 "\n\t" +
                 "{} is running! " +
                 "Access URLs:" +
                 "\n\t" +
                 "Local: " +
                 "\t\t" +
                 "{}://localhost:{}{}" +
                 "\n\t" +
                 "External: " +
                 "\t" +
                 "{}://{}:{}{}" +
                 "\n\t" +
                 "Profile(s): " +
                 "\t" +
                 "{}" +
                 "\n" +
                 "----------------------------------------------------------",
                 env.getProperty("spring.application.name"),
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

    @NonNull
    private static String getHostAddress() {
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        return hostAddress;
    }

    @NonNull
    private static String getContextPath(Environment env) {
        return Optional.ofNullable(env.getProperty("server.servlet.context-path")).filter(StringUtils::isNotBlank)
                       .orElse("/");
    }

    @NonNull
    private static String getProtocol(Environment env) {
        return Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https")
                       .orElse("http");
    }
}
