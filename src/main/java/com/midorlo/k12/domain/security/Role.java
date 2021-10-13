package com.midorlo.k12.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Role.
 */
@Entity
@Table(name = "roles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    //    @Min(value = 6)
    //    @Max(value = 50)
    @Column(name = "i_18_n", nullable = false, unique = true)
    private String i18n;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_roles__clearances",
        joinColumns = @JoinColumn(name = "roles_id"),
        inverseJoinColumns = @JoinColumn(name = "clearances_id")
    )
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private Set<Clearance> clearances = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role id(Long id) {
        this.id = id;
        return this;
    }

    public String getI18n() {
        return this.i18n;
    }

    public Role i18n(String i18n) {
        this.i18n = i18n;
        return this;
    }

    public void seti18n(String i18n) {
        this.i18n = i18n;
    }

    public Set<Clearance> getClearances() {
        return this.clearances;
    }

    public Role clearances(Set<Clearance> clearances) {
        this.setClearances(clearances);
        return this;
    }

    public Role addClearances(Clearance clearance) {
        this.clearances.add(clearance);
        clearance.getRoles().add(this);
        return this;
    }

    public Role removeClearances(Clearance clearance) {
        this.clearances.remove(clearance);
        clearance.getRoles().remove(this);
        return this;
    }

    public void setClearances(Set<Clearance> clearances) {
        this.clearances = clearances;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", i18n='" + getI18n() + "'" +
            "}";
    }
}
