package com.revnext.controller.menu.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {

    private String name;
    private String title;
    private String url;
    private Integer order;
    private Integer level;
    private List<MenuResponse> children = new ArrayList<>();
}
