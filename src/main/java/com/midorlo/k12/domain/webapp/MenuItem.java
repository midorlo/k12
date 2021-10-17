package com.midorlo.k12.domain.webapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.midorlo.k12.domain.security.Clearance;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MenuItem.
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "menu_items")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MenuItem implements Serializable {

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
    @Column(name = "target", nullable = false)
    private String target;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

//    @ManyToOne
//    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
//    private Clearance requiredClearance;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent", "requiredClearance", "childMenus", "childItems" }, allowSetters = true)
    private Menu parent;

    public MenuItem id(Long id) {
        this.id = id;
        return this;
    }

    public MenuItem i18n(String i18n) {
        this.i18n = i18n;
        return this;
    }

    public MenuItem icon(String icon) {
        this.icon = icon;
        return this;
    }

    public MenuItem target(String target) {
        this.target = target;
        return this;
    }

    public MenuItem enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

//    public MenuItem requiredClearance(Clearance clearance) {
//        this.setRequiredClearance(clearance);
//        return this;
//    }

    public MenuItem parent(Menu menu) {
        this.setParent(menu);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MenuItem menuItem = (MenuItem) o;
        return id != null && Objects.equals(id, menuItem.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
