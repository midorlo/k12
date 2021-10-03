package com.midorlo.k12.config.logging;

import com.midorlo.k12.aop.logging.LoggingAspect;
import com.midorlo.k12.config.application.ApplicationConstants;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

    @Bean
    @Profile(ApplicationConstants.SPRING_PROFILE_DEVELOPMENT)
    public LoggingAspect loggingAspect(Environment env) {
        return new LoggingAspect(env);
    }
}
