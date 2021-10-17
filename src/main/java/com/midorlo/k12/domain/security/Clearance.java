package com.midorlo.k12.domain.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

import static com.midorlo.k12.configuration.ApplicationConstants.TableNames.CLEARANCES;

/**
 * An authority (a web role) used by Spring Security.
 */
@Entity
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Table(name = CLEARANCES)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Clearance implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50, unique = true, updatable = false)
    private String name;

    //<editor-fold desc="equals / hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Clearance clearance = (Clearance) o;
        return name != null && Objects.equals(name, clearance.name);
    }

    @Override
    public int hashCode() {
        return 0;
    }
    //</editor-fold>
}
