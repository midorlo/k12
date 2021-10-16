package com.midorlo.k12.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Clearance.
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "clearances")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Clearance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "i_18_n", nullable = false, unique = true)
    private String i18n;

    @ManyToMany(mappedBy = "clearances")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "clearances" }, allowSetters = true)
    private Set<Role> roles = new HashSet<>();

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
