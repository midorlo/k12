package com.midorlo.k12.beans;

import static com.midorlo.k12.configuration.ApplicationConstants.SecurityConstants.ADMIN;
import static com.midorlo.k12.configuration.ApplicationConstants.SecurityConstants.USER;

import com.midorlo.k12.domain.security.Clearance;
import com.midorlo.k12.domain.security.Role;
import com.midorlo.k12.domain.security.User;
import com.midorlo.k12.repository.UserRepository;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner to install the default User Accounts
 */
@Slf4j
@Component
public class InstallUsersAndAuthorities implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public InstallUsersAndAuthorities(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        userRepository
            .findAll()
            .stream()
            .filter(u -> u.getLogin().equalsIgnoreCase("Admin"))
            .findFirst()
            .ifPresentOrElse(
                user -> log.warn("Accounts already installed!"),
                () ->
                    log.info(
                        "Installed Accounts: {}",
                        userRepository.saveAll(
                            List.of(
                                new User()
                                    .setActivated(true)
                                    .setLogin("admin")
                                    .setPasswordHash(passwordEncoder.encode("admin"))
                                    .setEmail("admin@midorlo.com")
                                    .setLangKey("de")
                                    .setFirstName("-")
                                    .setLastName("-")
                                    .setRoles(Set.of(new Role().setName(ADMIN).setClearances(Set.of(new Clearance().setName(ADMIN))))),
                                new User()
                                    .setActivated(true)
                                    .setLogin("user")
                                    .setPasswordHash(passwordEncoder.encode("user"))
                                    .setEmail("user@midorlo.com")
                                    .setLangKey("de")
                                    .setFirstName("-")
                                    .setLastName("-")
                                    .setRoles(Set.of(new Role().setName(USER).setClearances(Set.of(new Clearance().setName(USER)))))
                            )
                        )
                    )
            );
    }
}
