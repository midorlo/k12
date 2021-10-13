package com.midorlo.k12.config.database.init;

import com.midorlo.k12.domain.security.User;
import com.midorlo.k12.repository.AuthorityRepository;
import com.midorlo.k12.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CommandLineRunner to install the default User Accounts
 */
@Slf4j
@Component
public class InstallUsersAndAuthorities implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public InstallUsersAndAuthorities(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
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
                                    .setPassword(passwordEncoder.encode("admin"))
                                    .setEmail("admin@midorlo.com")
                                    .setLangKey("de")
                                    .setFirstName("-")
                                    .setLastName("-"),
                                new User()
                                    .setActivated(true)
                                    .setLogin("user")
                                    .setPassword(passwordEncoder.encode("user"))
                                    .setEmail("user@midorlo.com")
                                    .setLangKey("de")
                                    .setFirstName("-")
                                    .setLastName("-")
                            )
                        )
                    )
            );
    }
}
