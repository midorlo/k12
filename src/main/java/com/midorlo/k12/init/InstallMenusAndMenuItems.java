package com.midorlo.k12.init;

import com.midorlo.k12.domain.webapp.Menu;
import com.midorlo.k12.domain.webapp.MenuItem;
import com.midorlo.k12.repository.MenuItemRepository;
import com.midorlo.k12.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class InstallMenusAndMenuItems implements CommandLineRunner,
                                                 Ordered {

    private final MenuRepository     menuRepository;
    private final MenuItemRepository menuItemRepository;

    public InstallMenusAndMenuItems(MenuRepository menuRepository, MenuItemRepository menuItemRepository) {
        this.menuRepository     = menuRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) {
        List<Menu> menus = menuRepository.findAll();
        installAccountMenu(menus);
        installAdminMenu(menus);
    }

    private void installAdminMenu(Collection<Menu> allStoredMenus) {
        allStoredMenus
            .stream()
            .filter(m -> m.getI18n().equals("Administration"))
            .findFirst()
            .ifPresentOrElse(
                menu1 -> log.info("Admin Menu already installed"),
                () -> {
                    Menu m = menuRepository.save(new Menu().setEnabled(true).setIcon("home").setI18n("Administration"));
                    log.info("Created {}", m);
                    log.info(
                        "Created {}",
                        menuItemRepository.save(
                            new MenuItem()
                                .setParent(m)
                                .setIcon("analytics")
                                .setEnabled(true)
                                .setTarget("/metrics")
                                .setI18n("global.menu.admin.analytics")
                        )
                    );
                    log.info(
                        "Created {}",
                        menuItemRepository.save(
                            new MenuItem()
                                .setParent(m)
                                .setIcon("health_and_safety")
                                .setEnabled(true)
                                .setTarget("/health")
                                .setI18n("global.menu.admin.health_and_safety")
                        )
                    );
                    log.info(
                        "Created {}",
                        menuItemRepository.save(
                            new MenuItem()
                                .setParent(m)
                                .setIcon("analytics")
                                .setEnabled(true)
                                .setTarget("/metrics")
                                .setI18n("global.menu.admin.health")
                        )
                    );
                    log.info(
                        "Created {}",
                        menuItemRepository.save(
                            new MenuItem()
                                .setParent(m)
                                .setIcon("text_snippet")
                                .setEnabled(true)
                                .setTarget("/logs")
                                .setI18n("global.menu.admin.logs")
                        )
                    );
                    log.info(
                        "Created {}",
                        menuItemRepository.save(
                            new MenuItem()
                                .setParent(m)
                                .setIcon("'people'")
                                .setEnabled(true)
                                .setTarget("/users")
                                .setI18n("global.menu.admin.userManagement")
                        )
                    );
                }
            );
    }

    private void installAccountMenu(Collection<Menu> allStoredMenus) {
        Menu menuAccount = new Menu().setEnabled(true).setIcon("home").setI18n("Account");
        allStoredMenus
            .stream()
            .filter(m -> m.getI18n().equals("Account"))
            .findFirst()
            .ifPresentOrElse(
                menu1 -> log.info("Account Menu already installed"),
                () -> {
                    Menu m = menuRepository.save(menuAccount);
                    log.info("Created {}", m);
                    log.info(
                        "Created {}",
                        menuItemRepository.save(
                            new MenuItem()
                                .setParent(m)
                                .setIcon("account_circle")
                                .setEnabled(true)
                                .setTarget("/account")
                                .setI18n("global.menu.account" + ".main")
                        )
                    );
                    log.info(
                        "Created {}",
                        menuItemRepository.save(
                            new MenuItem()
                                .setParent(m)
                                .setIcon("password")
                                .setEnabled(true)
                                .setTarget("/password")
                                .setI18n("global.menu.account" + ".password")
                        )
                    );
                }
            );
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
