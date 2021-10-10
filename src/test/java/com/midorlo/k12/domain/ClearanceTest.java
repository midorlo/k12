package com.midorlo.k12.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.midorlo.k12.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClearanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Clearance.class);
        Clearance clearance1 = new Clearance();
        clearance1.setId(1L);
        Clearance clearance2 = new Clearance();
        clearance2.setId(clearance1.getId());
        assertThat(clearance1).isEqualTo(clearance2);
        clearance2.setId(2L);
        assertThat(clearance1).isNotEqualTo(clearance2);
        clearance1.setId(null);
        assertThat(clearance1).isNotEqualTo(clearance2);
    }
}
