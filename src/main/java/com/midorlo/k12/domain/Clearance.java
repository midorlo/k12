package com.midorlo.k12.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Clearance.
 */
@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clearance id(Long id) {
        this.id = id;
        return this;
    }

    public String getI18n() {
        return this.i18n;
    }

    public Clearance i18n(String i18n) {
        this.i18n = i18n;
        return this;
    }

    public void seti18n(String i18n) {
        this.i18n = i18n;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public Clearance roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public Clearance addRoles(Role role) {
        this.roles.add(role);
        role.getClearances().add(this);
        return this;
    }

    public Clearance removeRoles(Role role) {
        this.roles.remove(role);
        role.getClearances().remove(this);
        return this;
    }

    public void setRoles(Set<Role> roles) {
        if (this.roles != null) {
            this.roles.forEach(i -> i.removeClearances(this));
        }
        if (roles != null) {
            roles.forEach(i -> i.addClearances(this));
        }
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Clearance)) {
            return false;
        }
        return id != null && id.equals(((Clearance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Clearance{" +
            "id=" + getId() +
            ", i18n='" + getI18n() + "'" +
            "}";
    }
}
