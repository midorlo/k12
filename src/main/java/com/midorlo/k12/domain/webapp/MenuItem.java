package com.midorlo.k12.domain.webapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Table(name = "menu_items", schema = "webapp")
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Column(name = "default_title", nullable = false)
    private String i18nKey;

    @Column(name = "target_uri", nullable = false)
    private String to;

    @Column(name = "icon", nullable = false)
    private String icon;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
               optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    @JsonIgnore
    private Menu menu;

}
