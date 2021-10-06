package com.midorlo.k12.beans.webapp;

import com.midorlo.k12.domain.webapp.Menu;
import com.midorlo.k12.domain.webapp.MenuItem;
import com.midorlo.k12.repository.webapp.MenuItemRepository;
import com.midorlo.k12.repository.webapp.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class InstallMenus implements CommandLineRunner {

    private final MenuRepository     menuRepository;
    private final MenuItemRepository menuItemRepository;

    public InstallMenus(MenuRepository menuRepository,
                        MenuItemRepository menuItemRepository) {
        this.menuRepository     = menuRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        List<Menu> menus = menuRepository.findAll();
        installAccountMenu(menus);
        installAdminMenu(menus);
    }

    private void installAdminMenu(List<Menu> allStoredMenus) {
        allStoredMenus.stream()
             .filter(m -> m.getDefaultTitle().equals("Administration"))
             .findFirst()
             .ifPresentOrElse(
                     menu1 -> log.info("Admin Menu already installed"),
                     () -> {
                         Menu m = menuRepository.save(new Menu().setEnabled(true)
                                                                .setDefaultTitle("Administration"));
                         log.info("Created {}", m);
                         log.info("Created {}", menuItemRepository.save(new MenuItem()
                                                                                .setMenu(m)
                                                                                .setIcon("analytics")
                                                                                .setEnabled(true)
                                                                                .setTo("/metrics")
                                                                                .setI18nKey("global.menu" +
                                                                                            ".admin.health")));
                         log.info("Created {}", menuItemRepository.save(new MenuItem()
                                                                                .setMenu(m)
                                                                                .setIcon("health_and_safety")
                                                                                .setEnabled(true)
                                                                                .setTo("/health")
                                                                                .setI18nKey("global.menu" +
                                                                                            ".admin.metrics")));
                         log.info("Created {}", menuItemRepository.save(new MenuItem()
                                                                                .setMenu(m)
                                                                                .setIcon("analytics")
                                                                                .setEnabled(true)
                                                                                .setTo("/metrics")
                                                                                .setI18nKey("global.menu" +
                                                                                            ".admin.health")));
                         log.info("Created {}", menuItemRepository.save(new MenuItem()
                                                                                .setMenu(m)
                                                                                .setIcon("text_snippet")
                                                                                .setEnabled(true)
                                                                                .setTo("/logs")
                                                                                .setI18nKey("global.menu" +
                                                                                            ".admin.logs")));
                         log.info("Created {}", menuItemRepository.save(new MenuItem()
                                                                                .setMenu(m)
                                                                                .setIcon("'people'")
                                                                                .setEnabled(true)
                                                                                .setTo("/users")
                                                                                .setI18nKey("global.menu" +
                                                                                            ".admin" +
                                                                                            ".userManagement"
                                                                                )));

                     });
    }

    private void installAccountMenu(List<Menu> allStoredMenus) {
        allStoredMenus.stream()
             .filter(m -> m.getDefaultTitle().equals("Account"))
             .findFirst()
             .ifPresentOrElse(
                     menu1 -> log.info("Account Menu already installed"),
                     () -> {
                         Menu m = menuRepository.save(new Menu().setEnabled(true)
                                                                .setDefaultTitle("Account"));
                         log.info("Created {}", m);
                         log.info("Created {}", menuItemRepository.save(new MenuItem()
                                                                                .setMenu(m)
                                                                                .setIcon("account_circle")
                                                                                .setEnabled(true)
                                                                                .setTo("/account")
                                                                                .setI18nKey("global.menu.account.main")));
                         log.info("Created {}", menuItemRepository.save(new MenuItem()
                                                                                .setMenu(m)
                                                                                .setIcon("password")
                                                                                .setEnabled(true)
                                                                                .setTo("/password")
                                                                                .setI18nKey("global.menu.account.password")));
                     });
    }
}
