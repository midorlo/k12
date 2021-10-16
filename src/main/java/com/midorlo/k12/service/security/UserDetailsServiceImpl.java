package com.midorlo.k12.service.security;

import com.midorlo.k12.domain.security.User;
import com.midorlo.k12.repository.UserRepository;
import com.midorlo.k12.service.security.exception.UserNotActivatedException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Slf4j
@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return userRepository
                .findOneWithAuthoritiesByEmailIgnoreCase(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the " +
                                                                 "database"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.GERMAN);
        return userRepository
            .findOneWithAuthoritiesByLogin(lowercaseLogin)
            .map(user -> createSpringSecurityUser(lowercaseLogin, user))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
                                                             "database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin,
                                                                                        User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        return new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getPassword(),
            user.getSpringSecurityAuthorities()
        );
    }
}
