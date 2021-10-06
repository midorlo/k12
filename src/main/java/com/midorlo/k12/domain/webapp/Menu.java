package com.midorlo.k12.domain.webapp;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

@Table(name = "menus", schema = "webapp")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Column(name = "default_title", nullable = false)
    private String defaultTitle;

    @OneToMany(fetch = FetchType.EAGER,
               mappedBy = "menu",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private Set<MenuItem> menuItems;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private Menu parent;

}
