package com.midorlo.k12.domain.util;

import java.sql.Types;
import org.hibernate.dialect.H2Dialect;

/**
 * <p>FixedH2Dialect class.</p>
 */
public class FixedH2Dialect extends H2Dialect {

    /**
     * <p>Constructor for FixedH2Dialect.</p>
     */
    public FixedH2Dialect() {
        super();
        registerColumnType(Types.FLOAT, "real");
    }
}
