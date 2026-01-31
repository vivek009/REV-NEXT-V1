package com.revnext.controller.catalog.response;


import lombok.Data;

import java.util.UUID;

@Data
public class SuiteResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID formulaId;
    private UUID familyId;
    private String remark;
}
