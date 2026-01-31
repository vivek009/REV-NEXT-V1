package com.revnext.controller.menu;

import com.revnext.constants.Roles;
import com.revnext.controller.BaseController;
import com.revnext.controller.menu.request.ChildOpRequest;
import com.revnext.controller.menu.request.MenuRequest;
import com.revnext.controller.menu.request.MenuRequestMapper;
import com.revnext.controller.menu.request.UpdateMenuRequest;
import com.revnext.controller.menu.response.MenuResponse;
import com.revnext.controller.menu.response.MenuResponseMapper;
import com.revnext.domain.menu.Menu;
import com.revnext.service.menu.MenuService;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menus")
@Slf4j
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuResponseMapper responseMapper;

    @Autowired
    private MenuRequestMapper requestMapper;

    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenuForRole(@RequestAttribute(name = "Roles") Roles roleName) {
        // Get entity list from service
        List<Menu> menus = menuService.getMenuByRole(roleName);

        // Convert to DTO here using the mapper

        return getResponse(() -> responseMapper.toDtoList(menus));
    }

    @PostMapping(path = "/menu")
    public ResponseEntity<Boolean> addMenu(@RequestBody MenuRequest request) {
        return getResponse(() -> menuService.createMenu(requestMapper.toMenu(request)));
    }

    @GetMapping(path = "/menu")
    public ResponseEntity<MenuResponse> getMenu(@RequestParam(name = "menuId") String id) {
        Menu menu = menuService.getMenuById(UUID.fromString(id));
        return getResponse(() -> responseMapper.toDto(menu));
    }

    @DeleteMapping(path = "/menu/{menuId}")
    public ResponseEntity<String> removeMenu(@PathVariable String menuId) {
        return getResponse(() -> {
            menuService.deleteMenu(menuId);
            return "Menu deleted successfully";
        });
    }

    @PutMapping(path = "/menu")
    public ResponseEntity<String> updateMenu(@RequestBody UpdateMenuRequest updateMenuRequest) {
        return getResponse(() -> {
            menuService.updateMenu(requestMapper.toMenu(updateMenuRequest.getMenuRequest()), updateMenuRequest.getMenuId());
            return "Menu updated successfully";
        });
    }


    @PutMapping(path = "/childMenu")
    public ResponseEntity<Boolean> addChildMenu(@RequestBody ChildOpRequest childOpRequest) {
        return getResponse(() -> menuService.addChildren(childOpRequest.getParentId(), childOpRequest.getChildrenIds()));
    }

    @DeleteMapping(path = "/childMenu")
    public ResponseEntity<Boolean> removeChildMenu(@RequestBody ChildOpRequest childOpRequest) {
        return getResponse(() -> menuService.removeChildren(childOpRequest.getParentId(), childOpRequest.getChildrenIds()));
    }

    @PostMapping("/menu/{menuId}/{role}")
    public ResponseEntity<String> assignMenuToRole(@PathVariable String menuId, @PathVariable Roles role) {
        return getResponse(() -> {
            menuService.assignMenuToRole(menuId, role);
            return "Role added successfully";
        });
    }

    @DeleteMapping("/menu/{menuId}/{role}")
    public ResponseEntity<String> removeMenuFromRole(@PathVariable String menuId, @PathVariable Roles role) {
        return getResponse(() -> {
            menuService.removeMenuFromRole(menuId, role);
            return "Role removed successfully";
        });
    }


}
