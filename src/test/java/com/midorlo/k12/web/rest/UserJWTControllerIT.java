package com.midorlo.k12.web.rest;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.midorlo.k12.util.IntegrationTest;
import com.midorlo.k12.domain.security.User;
import com.midorlo.k12.repository.UserRepository;
import com.midorlo.k12.web.rest.auth.model.LoginVM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the  REST controller.
 */
@AutoConfigureMockMvc
@IntegrationTest
class UserJWTControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    void testAuthorize() throws Exception {
        User user = new User();
        user.setLogin("user-filter-controller");
        user.setEmail("user-filter-controller@example.com");
        user.setActivated(true);
        user.setPasswordHash(passwordEncoder.encode("test"));

        userRepository.saveAndFlush(user);

        LoginVM login = new LoginVM();
        login.setUsername("user-filter-controller");
        login.setPassword("test");
        mockMvc
            .perform(
                post("/api/identity/authenticate").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id_token").isString())
            .andExpect(jsonPath("$.id_token").isNotEmpty())
            .andExpect(header().string("Authorization", not(nullValue())))
            .andExpect(header().string("Authorization", not(is(emptyString()))));
    }

    @Test
    @Transactional
    void testAuthorizeWithRememberMe() throws Exception {
        User user = new User();
        user.setLogin("user-filter-controller-remember-me");
        user.setEmail("user-filter-controller-remember-me@example.com");
        user.setActivated(true);
        user.setPasswordHash(passwordEncoder.encode("test"));

        userRepository.saveAndFlush(user);

        LoginVM login = new LoginVM();
        login.setUsername("user-filter-controller-remember-me");
        login.setPassword("test");
        login.setRememberMe(true);
        mockMvc
            .perform(
                post("/api/identity/authenticate").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id_token").isString())
            .andExpect(jsonPath("$.id_token").isNotEmpty())
            .andExpect(header().string("Authorization", not(nullValue())))
            .andExpect(header().string("Authorization", not(is(emptyString()))));
    }

    @Test
    void testAuthorizeFails() throws Exception {
        LoginVM login = new LoginVM();
        login.setUsername("wrong-user");
        login.setPassword("wrong password");
        mockMvc
            .perform(
                post("/api/identity/authenticate").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login))
            )
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.id_token").doesNotExist())
            .andExpect(header().doesNotExist("Authorization"));
    }
}
