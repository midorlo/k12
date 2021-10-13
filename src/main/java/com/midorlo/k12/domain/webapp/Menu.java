package com.midorlo.k12.domain.webapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

import com.midorlo.k12.domain.security.Clearance;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Menu.
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "menus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "i_18_n", nullable = false, unique = true)
    private String i18n;

    @Column(name = "icon")
    private String icon;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent", "requiredClearance", "childMenus", "childItems" }, allowSetters = true)
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

    public Menu id(Long id) {
        this.id = id;
        return this;
    }

    public String getI18n() {
        return this.i18n;
    }

    public Menu i18n(String i18n) {
        this.i18n = i18n;
        return this;
    }

    public Menu icon(String icon) {
        this.icon = icon;
        return this;
    }

    public Menu enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Menu parent(Menu menu) {
        this.setParent(menu);
        return this;
    }

    public Menu requiredClearance(Clearance clearance) {
        this.setRequiredClearance(clearance);
        return this;
    }

    public Menu childMenus(Set<Menu> menus) {
        this.setChildMenus(menus);
        return this;
    }

    public Menu addChildMenus(Menu menu) {
        this.childMenus.add(menu);
        menu.setParent(this);
        return this;
    }

    public Menu removeChildMenus(Menu menu) {
        this.childMenus.remove(menu);
        menu.setParent(null);
        return this;
    }

    public Menu childItems(Set<MenuItem> menuItems) {
        this.setChildItems(menuItems);
        return this;
    }

    public Menu addChildItems(MenuItem menuItem) {
        this.childItems.add(menuItem);
        menuItem.setParent(this);
        return this;
    }

    public Menu removeChildItems(MenuItem menuItem) {
        this.childItems.remove(menuItem);
        menuItem.setParent(null);
        return this;
    }


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
