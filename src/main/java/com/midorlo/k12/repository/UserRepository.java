package com.midorlo.k12.repository;

import com.midorlo.k12.configuration.ApplicationConstants;
import com.midorlo.k12.domain.security.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "clearances")
    @Cacheable(cacheNames = ApplicationConstants.CacheNames.USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithClearancesByLogin(String login);

    @EntityGraph(attributePaths = "clearances")
    @Cacheable(cacheNames = ApplicationConstants.CacheNames.USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithClearancesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
}
