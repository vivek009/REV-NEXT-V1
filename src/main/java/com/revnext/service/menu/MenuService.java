package com.revnext.service.menu;

import com.revnext.constants.Roles;
import com.revnext.domain.menu.Menu;
import com.revnext.domain.user.Role;
import com.revnext.repository.MenuRepository;
import com.revnext.repository.user.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private UserRoleRepository roleRepository;

    @Cacheable(value = "menuCache", key = "#roleName")
    public List<Menu> getMenuByRole(Roles roleName) {
        List<Menu> flatMenus = menuRepository.findMenusByRoleName(roleName);
        return buildHierarchy(flatMenus);
    }

    private List<Menu> buildHierarchy(List<Menu> flatMenus) {
        Map<UUID, Menu> menuMap = new HashMap<>();
        List<Menu> rootMenus = new ArrayList<>();

        for (Menu menu : flatMenus) {
            menu.getChildren().clear(); // Avoid previous children if any
            menuMap.put(menu.getId(), menu);
        }

        for (Menu menu : flatMenus) {
            if (menu.getParent() != null && menuMap.containsKey(menu.getParent().getId())) {
                menuMap.get(menu.getParent().getId()).getChildren().add(menu);
            } else {
                rootMenus.add(menu);
            }
        }

        rootMenus.sort(Comparator.comparing(Menu::getOrder));
        return rootMenus;
    }

    public boolean createMenu(Menu menu) {
        if (menuRepository.save(menu) != null) {
            log.info("Menu created successfully");
            return true;

        } else {
            log.error("Menu creation  failed");
            return false;
        }
    }

    public Menu getMenuById(UUID id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
    }

    public boolean updateMenu(Menu updatedMenu, String id) {
        Menu existingMenu = menuRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("Menu Not found"));

        if (updatedMenu.getName() != null)
            existingMenu.setName(updatedMenu.getName());
        if (updatedMenu.getTitle() != null)
            existingMenu.setTitle(updatedMenu.getTitle());
        if (updatedMenu.getUrl() != null)
            existingMenu.setUrl(updatedMenu.getUrl());
        if (updatedMenu.getOrder() != null)
            existingMenu.setOrder(updatedMenu.getOrder());
        if (updatedMenu.getLevel() != null)
            existingMenu.setLevel(updatedMenu.getLevel());
        existingMenu.setDisabled(updatedMenu.isDisabled());
        if (menuRepository.save(existingMenu) != null) {
            log.info("Menu updated successfully");
            return true;

        } else {
            log.error("Menu update  failed");
            return false;
        }

    }

    public void deleteMenu(String id) {
        Menu existingMenu = menuRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("menu not found"));
        menuRepository.delete(existingMenu);
    }

    @Transactional
    public boolean addChildren(String parentId, List<String> childrenIds) {

        Menu parentMenu = menuRepository.findById(UUID.fromString(parentId))
                .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
        List<Menu> children = new ArrayList<>();
        List<Menu> existingChildren = parentMenu.getChildren();
        for (String childId : childrenIds) {
            Menu childMenu = menuRepository.findById(UUID.fromString(childId))
                    .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
            childMenu.setParent(parentMenu);
            childMenu.setLevel(parentMenu.getLevel() + 1);
            children.add(childMenu);
            menuRepository.save(childMenu);

        }
        existingChildren.addAll(children);
        parentMenu.setChildren(existingChildren);
        log.info("Adding children");
        if (menuRepository.save(parentMenu) != null) {
            log.info("Menu created successfully");
            return true;
        } else {
            log.error("Menu creation  failed");
            return false;
        }
    }

    @Transactional
    public boolean removeChildren(String parentId, List<String> childrenIds) {
        Menu parentMenu = menuRepository.findById(UUID.fromString(parentId))
                .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
        List<Menu> children = new ArrayList<>();
        List<Menu> existingChildren = parentMenu.getChildren();
        for (String childId : childrenIds) {
            Menu childMenu = menuRepository.findById(UUID.fromString(childId))
                    .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
            if (parentMenu.getChildren().stream().anyMatch(child -> child.getId().equals(UUID.fromString(childId)))) {
                childMenu.setParent(null);
                childMenu.setLevel(-1);
                children.add(childMenu);
                menuRepository.save(childMenu);
            } else {

                log.error("{} menu doesn't contains child menu{}", parentMenu.getName(), childMenu.getName());
            }

        }
        existingChildren.removeAll(children);
        parentMenu.setChildren(existingChildren);
        log.info("Removing children");
        if (menuRepository.save(parentMenu) != null) {
            log.info("Menu created successfully");
            return true;
        } else {
            log.error("Menu creation  failed");
            return false;
        }
    }

    public boolean assignMenuToRole(String menuId, Roles role) {
        Menu menu = menuRepository.findById(UUID.fromString(menuId))
                .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
        Role user = roleRepository.findByName(role)
                .orElseThrow(() -> new EntityNotFoundException("User with Role" + role + "not found"));
        Set<Role> existingUsers = menu.getRoles();
        existingUsers.add(user);
        menu.setRoles(existingUsers);
        if (menuRepository.save(menu) != null) {
            log.info("Menu created successfully");
            return true;
        } else {
            log.error("Menu creation  failed");
            return false;
        }
    }

    public boolean removeMenuFromRole(String menuId, Roles role) {
        Menu menu = menuRepository.findById(UUID.fromString(menuId))
                .orElseThrow(() -> new EntityNotFoundException("Menu not found"));
        Role user = roleRepository.findByName(role)
                .orElseThrow(() -> new EntityNotFoundException("User with Role" + role + "not found"));
        Set<Role> existingUsers = menu.getRoles();
        existingUsers.remove(user);
        menu.setRoles(existingUsers);
        if (menuRepository.save(menu) != null) {
            log.info("Menu created successfully");
            return true;
        } else {
            log.error("Menu creation  failed");
            return false;
        }
    }

}
