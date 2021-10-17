package com.midorlo.k12.service.security;

import com.midorlo.k12.domain.security.Clearance;
import com.midorlo.k12.domain.security.Role;
import com.midorlo.k12.domain.security.User;
import com.midorlo.k12.repository.UserRepository;
import com.midorlo.k12.service.security.exception.UserNotActivatedException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .findOneWithClearancesByEmailIgnoreCase(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the "
                                                                 + "database"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.GERMAN);
        return userRepository
            .findOneWithClearancesByLogin(lowercaseLogin)
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
            user.getPasswordHash(),
            createSpringSecurityAuthorities(user)
        );
    }

    private static List<GrantedAuthority> createSpringSecurityAuthorities(@NonNull User user) {
        return Stream.concat(user.getRoles()
                                 .stream()
                                 .map(Role::getClearances)
                                 .flatMap(Collection::stream)
                                 .map(Clearance::getName),
                             user.getClearances()
                                 .stream()
                                 .map(Clearance::getName))
                     .distinct()
                     .map(SimpleGrantedAuthority::new)
                     .collect(Collectors.toList());
    }
}
