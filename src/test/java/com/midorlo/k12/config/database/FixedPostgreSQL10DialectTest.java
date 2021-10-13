package com.midorlo.k12.config.database;

import static org.assertj.core.api.Assertions.assertThat;

import com.midorlo.k12.LogbackRecorder;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.midorlo.k12.config.database.FixedPostgreSQL10Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.BlobTypeDescriptor;
import org.hibernate.type.descriptor.sql.BooleanTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FixedPostgreSQL10DialectTest {

    private final List<LogbackRecorder> recorders = new LinkedList<>();

    private final Map<Integer, String> registered = new LinkedHashMap<>();

    private FixedPostgreSQL10Dialect dialect;

    @BeforeEach
    void setup() {
        recorders.add(LogbackRecorder.forName("org.jboss.logging").reset().capture("ALL"));
        recorders.add(LogbackRecorder.forClass(Dialect.class).reset().capture("ALL"));

        dialect =
            new FixedPostgreSQL10Dialect() {
                @Override
                protected void registerColumnType(int code, String name) {
                    registered.put(code, name);
                    super.registerColumnType(code, name);
                }
            };
    }

    @AfterEach
    void teardown() {
        recorders.forEach(LogbackRecorder::release);
        recorders.clear();
        registered.clear();
    }

    @Test
    void testBlobTypeRegister() {
        assertThat(registered.get(Types.BLOB)).isEqualTo("bytea");
    }

    @Test
    void testBlobTypeRemap() {
        SqlTypeDescriptor descriptor = dialect.remapSqlTypeDescriptor(BlobTypeDescriptor.DEFAULT);
        assertThat(descriptor).isEqualTo(BinaryTypeDescriptor.INSTANCE);
    }

    @Test
    void testOtherTypeRemap() {
        SqlTypeDescriptor descriptor = dialect.remapSqlTypeDescriptor(BooleanTypeDescriptor.INSTANCE);
        assertThat(descriptor).isEqualTo(BooleanTypeDescriptor.INSTANCE);
    }
}
