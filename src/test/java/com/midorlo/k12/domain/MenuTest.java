package com.midorlo.k12.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.midorlo.k12.domain.webapp.Menu;
import com.midorlo.k12.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Menu.class);
        Menu menu1 = new Menu();

        //noinspection EqualsWithItself
        assertThat(menu1.equals(menu1)).isTrue();

        Menu menu2 = new Menu();
        menu2.setId(menu1.getId());
        assertThat(menu1).isNotEqualTo(menu2);

        menu2.setId(2L);
        assertThat(menu1).isNotEqualTo(menu2);

        menu1.setId(null);
        assertThat(menu1).isNotEqualTo(menu2);
    }
}
