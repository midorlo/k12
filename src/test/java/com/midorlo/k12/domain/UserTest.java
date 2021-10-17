package com.midorlo.k12.domain;

import com.midorlo.k12.domain.security.Clearance;
import com.midorlo.k12.domain.security.User;
import com.midorlo.k12.web.api.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Clearance.class);

        User user1 = new User();
        user1.setLogin("C1");

        User user2 = new User();
        user2.setLogin(user1.getLogin());

        assertThat(user1).isNotEqualTo(user2);

        user1.setId(1L);
        user2.setId(1L);
        assertThat(user1).isEqualTo(user2);

        user2.setLogin("C2");
        assertThat(user1).isEqualTo(user2);

        user1.setLogin(null);
        assertThat(user1).isEqualTo(user2);
    }
}
