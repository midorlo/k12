package com.midorlo.k12.configuration.web;

import com.midorlo.k12.configuration.ApplicationConstants;
import com.midorlo.k12.configuration.ApplicationProperties;
import com.midorlo.k12.configuration.web.filter.JwtFilterBean;
import com.midorlo.k12.service.security.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class WebSecurityConfigurerAdapterExt extends WebSecurityConfigurerAdapter {

    private final ApplicationProperties applicationProperties;
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    public WebSecurityConfigurerAdapterExt(
        TokenProvider tokenProvider,
        CorsFilter corsFilter,
        ApplicationProperties applicationProperties,
        SecurityProblemSupport problemSupport
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
            .ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/**")
            .antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .headers(h -> h.permissionsPolicy(p -> p.policy(ApplicationConstants.PERMISSIONS_POLICY_CONFIG)))
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
            .and()
            .headers()
            .contentSecurityPolicy(applicationProperties.getSecurity().getContentSecurityPolicy())
            .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            .and()
            .frameOptions()
            .deny()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            .authorizeRequests()
            .antMatchers("/api/identity").permitAll()
            .antMatchers("/api/identity/**").permitAll()

            .antMatchers("/api/webapp").permitAll()
            .antMatchers("/api/webapp/**").permitAll()

            .antMatchers("/api/admin").hasAuthority(ApplicationConstants.SecurityConstants.ROLE_ADMIN)
            .antMatchers("/api/admin/**").hasAuthority(ApplicationConstants.SecurityConstants.ROLE_ADMIN)

            .antMatchers("/management").permitAll()
            .antMatchers("/management/**").permitAll()
//            .antMatchers("/management/**").hasAuthority(ApplicationConstants.SecurityConstants.ADMIN)
//
//            .antMatchers("/management/health").permitAll()
//            .antMatchers("/management/health/**").permitAll()
//
//            .antMatchers("/management/actuator").permitAll()
//            .antMatchers("/management/actuator/**").permitAll()
//            .antMatchers("/management/prometheus").permitAll()
//            .antMatchers("/management/prometheus/**").permitAll()

            .and()
            .httpBasic()

            .and()
            .addFilterBefore(new JwtFilterBean(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        // @formatter:on
    }
}
