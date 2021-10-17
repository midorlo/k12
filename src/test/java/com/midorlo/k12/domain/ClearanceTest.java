package com.midorlo.k12.domain;

import com.midorlo.k12.domain.security.Clearance;
import com.midorlo.k12.web.api.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClearanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Clearance.class);
        Clearance clearance1 = new Clearance();
        clearance1.setName("C1");
        Clearance clearance2 = new Clearance();
        clearance2.setName(clearance1.getName());
        assertThat(clearance1).isEqualTo(clearance2);
        clearance2.setName("C2");
        assertThat(clearance1).isNotEqualTo(clearance2);
        clearance1.setName(null);
        assertThat(clearance1).isNotEqualTo(clearance2);
    }
}
