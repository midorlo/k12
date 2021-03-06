package com.midorlo.k12.configuration;

import lombok.experimental.UtilityClass;

import java.net.URI;

/**
 * Application constants.
 *
 * Reminder: These values are not environment ie dev,qa,stage specific and do not change throughout the lifecycle.
 */
@UtilityClass
public final class ApplicationConstants {

    /**
     * Regex for acceptable logins
     */
    public static final String LOGIN_REGEX =
        "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\" + ".[a-zA-Z0-9-]+)*)|(?>[_" + ".@A-Za-z0-9-]+)$";
    /**
     * Default System Identifier.
     */
    public static final String SYSTEM      = "system";

    /**
     * Global system language.
     */
    public static final String DEFAULT_LANGUAGE = "de";

    /**
     * Http feature policy configuration.
     */
    public static final String PERMISSIONS_POLICY_CONFIG =
        "geolocation 'none'; midi 'none'; sync-xhr " +
        "'none'; microphone 'none'; camera 'none'; " +
        "magnetometer 'none'; gyroscope 'none'; " +
        "fullscreen 'self'; payment 'none'";
    public static final String REPOSITORY_PACKAGE        = "com.midorlo.k12.repository";
    public static final String SPRING_PROFILE_DEFAULT    = "spring.profiles.default";

    public static final class ContextConstants {

        /**
         * Constant <code>SPRING_PROFILE_DEVELOPMENT="dev"</code>
         */
        public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
        /**
         * Constant <code>SPRING_PROFILE_TEST="test"</code>
         */
        public static final String SPRING_PROFILE_TEST        = "test";
        /**
         * Constant <code>SPRING_PROFILE_PRODUCTION="prod"</code>
         */
        public static final String SPRING_PROFILE_PRODUCTION  = "prod";
        /**
         * Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
         * Constant <code>SPRING_PROFILE_CLOUD="cloud"</code>
         */
        public static final String SPRING_PROFILE_CLOUD       = "cloud";
        /**
         * Spring profile used when deploying to Heroku
         * Constant <code>SPRING_PROFILE_HEROKU="heroku"</code>
         */
        public static final String SPRING_PROFILE_HEROKU      = "heroku";
        /**
         * Spring profile used when deploying to Amazon ECS
         * Constant <code>SPRING_PROFILE_AWS_ECS="aws-ecs"</code>
         */
        public static final String SPRING_PROFILE_AWS_ECS     = "aws-ecs";
        /**
         * Spring profile used when deploying to Microsoft Azure
         * Constant <code>SPRING_PROFILE_AZURE="azure"</code>
         */
        public static final String SPRING_PROFILE_AZURE       = "azure";
        /**
         * Spring profile used to enable OpenAPI doc generation
         * Constant <code>SPRING_PROFILE_API_DOCS="api-docs"</code>
         */
        public static final String SPRING_PROFILE_API_DOCS    = "api-docs";
        /**
         * Spring profile used when deploying to Kubernetes and OpenShift
         * Constant <code>SPRING_PROFILE_K8S="k8s"</code>
         */
        public static final String SPRING_PROFILE_K8S         = "k8s";
    }

    public static final class SecurityConstants {

        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_USER      = "ROLE_USER";
        public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    }

    public static final class ErrorConstants {

        public static final String ERR_CONCURRENCY_FAILURE   = "error.concurrencyFailure";
        public static final String ERR_VALIDATION            = "error.validation";
        public static final String PROBLEM_BASE_URL          = "https://www.application.tld/problem";
        public static final URI    DEFAULT_TYPE              = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
        public static final URI    CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
        public static final URI    INVALID_PASSWORD_TYPE     = URI.create(PROBLEM_BASE_URL + "/invalid-password");
        public static final URI    EMAIL_ALREADY_USED_TYPE   = URI.create(PROBLEM_BASE_URL + "/email-already-used");
        public static final URI    LOGIN_ALREADY_USED_TYPE   = URI.create(PROBLEM_BASE_URL + "/login-already-used");

        private ErrorConstants() {}
    }

    public static final class TechConstants {

        public static final String AUDITOR_BEAN = "springSecurityAuditorAware";
    }

    public static final class TableNames {
        public static final String CLEARANCES = "clearances";
        public static final String ROLES      = "roles";
        public static final String USERS      = "users";
        public static final String MENUS      = "menus";
        public static final String MENU_ITEMS = "menu_items";
    }

    public static final class ColumnNames {}

    public static final class RelationNames {}

    public static final class CacheNames {

        public static final String USERS_BY_LOGIN_CACHE = "usersByLogin";
        public static final String USERS_BY_EMAIL_CACHE = "usersByEmail";
    }
}
