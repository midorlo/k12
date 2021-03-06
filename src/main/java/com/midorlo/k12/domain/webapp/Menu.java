package com.midorlo.k12.domain.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.midorlo.k12.domain.security.Clearance;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.midorlo.k12.configuration.ApplicationConstants.TableNames.MENUS;

/**
 * A Menu.
 */

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = MENUS)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    private Long id;

    @Column(name = "i18n")
    private String i18n;

    @Column(name = "icon")
    private String icon;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @ManyToOne
    @JsonIgnore
    private Menu parent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private Clearance requiredClearance;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "requiredClearance", "childMenus", "childItems" }, allowSetters = true)
    private Set<Menu> childMenus = new HashSet<>();

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "requiredClearance", "parent" }, allowSetters = true)
    private Set<MenuItem> childItems = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        return id != null && id.equals(((Menu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Menu{" +
               "id=" + getId() +
               ", i18n='" + getI18n() + "'" +
               ", icon='" + getIcon() + "'" +
               ", enabled='" + getEnabled() + "'" +
               "}";
    }
}
