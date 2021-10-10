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
 * A Menu.
 */
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

    @NotNull
    @Column(name = "icon", nullable = false)
    private String icon;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

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

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "requiredClearance", "parent" }, allowSetters = true)
    private Set<MenuItem> childItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Menu id(Long id) {
        this.id = id;
        return this;
    }

    public String geti18n() {
        return this.i18n;
    }

    public Menu i18n(String i18n) {
        this.i18n = i18n;
        return this;
    }

    public void seti18n(String i18n) {
        this.i18n = i18n;
    }

    public String getIcon() {
        return this.icon;
    }

    public Menu icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public Menu enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Menu getParent() {
        return this.parent;
    }

    public Menu parent(Menu menu) {
        this.setParent(menu);
        return this;
    }

    public void setParent(Menu menu) {
        this.parent = menu;
    }

    public Clearance getRequiredClearance() {
        return this.requiredClearance;
    }

    public Menu requiredClearance(Clearance clearance) {
        this.setRequiredClearance(clearance);
        return this;
    }

    public void setRequiredClearance(Clearance clearance) {
        this.requiredClearance = clearance;
    }

    public Set<Menu> getChildMenus() {
        return this.childMenus;
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

    public void setChildMenus(Set<Menu> menus) {
        if (this.childMenus != null) {
            this.childMenus.forEach(i -> i.setParent(null));
        }
        if (menus != null) {
            menus.forEach(i -> i.setParent(this));
        }
        this.childMenus = menus;
    }

    public Set<MenuItem> getChildItems() {
        return this.childItems;
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

    public void setChildItems(Set<MenuItem> menuItems) {
        if (this.childItems != null) {
            this.childItems.forEach(i -> i.setParent(null));
        }
        if (menuItems != null) {
            menuItems.forEach(i -> i.setParent(this));
        }
        this.childItems = menuItems;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
            ", i18n='" + geti18n() + "'" +
            ", icon='" + getIcon() + "'" +
            ", enabled='" + getEnabled() + "'" +
            "}";
    }
}
