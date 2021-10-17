package com.midorlo.k12.domain.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Clearance clearance = (Clearance) o;
        return id != null && Objects.equals(id, clearance.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
