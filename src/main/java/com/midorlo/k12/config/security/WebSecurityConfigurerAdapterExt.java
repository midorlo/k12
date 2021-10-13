package com.midorlo.k12.config.security;

import com.midorlo.k12.config.ApplicationConstants;
import com.midorlo.k12.config.ApplicationProperties;
import com.midorlo.k12.config.security.jwt.JwtSecurityConfigurerAdapter;
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

    private final ApplicationProperties  applicationProperties;
    private final TokenProvider          tokenProvider;
    private final CorsFilter             corsFilter;
    private final SecurityProblemSupport problemSupport;

    public WebSecurityConfigurerAdapterExt(TokenProvider tokenProvider,
                                           CorsFilter corsFilter,
                                           ApplicationProperties applicationProperties,
                                           SecurityProblemSupport problemSupport) {
        this.tokenProvider         = tokenProvider;
        this.corsFilter            = corsFilter;
        this.problemSupport        = problemSupport;
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
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
            .antMatchers("/api/identity/**").permitAll()
            .antMatchers("/api/webapp/**").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/identity/account/reset-password/init").permitAll()
            .antMatchers("/api/identity/account/reset-password/finish").permitAll()
            .antMatchers("/api/admin/**").hasAuthority(ApplicationConstants.ADMIN)
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/health/**").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/**").hasAuthority(ApplicationConstants.ADMIN)
            .and()
            .httpBasic()
            .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JwtSecurityConfigurerAdapter securityConfigurerAdapter() {
        return new JwtSecurityConfigurerAdapter(tokenProvider);
    }
}
