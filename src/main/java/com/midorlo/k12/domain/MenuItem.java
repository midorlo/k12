package com.midorlo.k12.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MenuItem.
 */
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

    @ManyToOne
    @JsonIgnoreProperties(value = { "roles" }, allowSetters = true)
    private Clearance requiredClearance;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent", "requiredClearance", "childMenus", "childItems" }, allowSetters = true)
    private Menu parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MenuItem id(Long id) {
        this.id = id;
        return this;
    }

    public String geti18n() {
        return this.i18n;
    }

    public MenuItem i18n(String i18n) {
        this.i18n = i18n;
        return this;
    }

    public void seti18n(String i18n) {
        this.i18n = i18n;
    }

    public String getIcon() {
        return this.icon;
    }

    public MenuItem icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTarget() {
        return this.target;
    }

    public MenuItem target(String target) {
        this.target = target;
        return this;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public MenuItem enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Clearance getRequiredClearance() {
        return this.requiredClearance;
    }

    public MenuItem requiredClearance(Clearance clearance) {
        this.setRequiredClearance(clearance);
        return this;
    }

    public void setRequiredClearance(Clearance clearance) {
        this.requiredClearance = clearance;
    }

    public Menu getParent() {
        return this.parent;
    }

    public MenuItem parent(Menu menu) {
        this.setParent(menu);
        return this;
    }

    public void setParent(Menu menu) {
        this.parent = menu;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuItem)) {
            return false;
        }
        return id != null && id.equals(((MenuItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MenuItem{" +
            "id=" + getId() +
            ", i18n='" + geti18n() + "'" +
            ", icon='" + getIcon() + "'" +
            ", target='" + getTarget() + "'" +
            ", enabled='" + getEnabled() + "'" +
            "}";
    }
}
