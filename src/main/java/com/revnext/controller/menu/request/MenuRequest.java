package com.revnext.controller.menu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRequest {
    private String name;
    private String title;
    private String url;
    private Integer order;
    private Integer level;
    private boolean isDisabled;

}
