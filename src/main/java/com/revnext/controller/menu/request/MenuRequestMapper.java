package com.revnext.controller.menu.request;

import com.revnext.domain.menu.Menu;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MenuRequestMapper {

    public List<Menu> toMenuList(List<MenuRequest> menus) {
        return menus.stream()
                .map(this::toMenu)
                .collect(Collectors.toList());
    }

    public Menu toMenu(MenuRequest request) {
        return Menu.builder().name(request.getName())
                .level(request.getLevel())
                .title(request.getTitle())
                .url(request.getUrl())
                .level(request.getLevel())
                .order(request.getOrder())
                .disabled(request.isDisabled())
                .build();
    }
}
