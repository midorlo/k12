package com.midorlo.k12.init;

import com.midorlo.k12.domain.security.Authority;
import com.midorlo.k12.domain.security.Clearance;
import com.midorlo.k12.domain.security.Role;
import com.midorlo.k12.domain.security.User;
import com.midorlo.k12.repository.AuthorityRepository;
import com.midorlo.k12.repository.RoleRepository;
import com.midorlo.k12.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * CommandLineRunner to install the default User Accounts
 */
@Slf4j
@Component
public class InstallUsersAndAuthorities implements CommandLineRunner {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    public InstallUsersAndAuthorities(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        RoleRepository roleRepository, AuthorityRepository authorityRepository
    ) {
        this.userRepository  = userRepository;
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
                                    .setPassword(passwordEncoder.encode("admin"))
                                    .setEmail("admin@midorlo.com")
                                    .setLangKey("de")
                                    .setFirstName("-")
                                    .setLastName("-")
                                    .setAuthorities(Set.of(new Authority().setName("ADMIN"),
                                                           new Authority().setName("ROLE_ADMIN")
                                                    )
                                    )
                                    .setRoles(Set.of(new Role()
                                                         .setI18n("ADMIN")
                                                         .setClearances(Set.of(new Clearance().setI18n("nouns.admin"))))),
                                new User()
                                    .setActivated(true)
                                    .setLogin("user")
                                    .setPassword(passwordEncoder.encode("user"))
                                    .setEmail("user@midorlo.com")
                                    .setLangKey("de")
                                    .setFirstName("-")
                                    .setLastName("-")
                                    .setRoles(Set.of(new Role()
                                                         .setI18n("USER")
                                                         .setClearances(Set.of(new Clearance().setI18n("nouns.user")))))
                            )
                        )
                    )
            );
    }
}
