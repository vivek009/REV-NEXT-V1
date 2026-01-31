package com.revnext.controller.menu.response;

import com.revnext.domain.menu.Menu;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MenuResponseMapper {

    public MenuResponse toDto(Menu menu) {
        MenuResponse dto = new MenuResponse();
        dto.setName(menu.getName());
        dto.setTitle(menu.getTitle());
        dto.setUrl(menu.getUrl());
        dto.setOrder(menu.getOrder());
        dto.setLevel(menu.getLevel());

        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            List<MenuResponse> children = menu.getChildren().stream()
                    .map(this::toDto)
                    .sorted(Comparator.comparing(MenuResponse::getOrder))
                    .collect(Collectors.toList());
            dto.setChildren(children);
        }
        return dto;
    }

    public List<MenuResponse> toDtoList(List<Menu> menus) {
        return menus.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
