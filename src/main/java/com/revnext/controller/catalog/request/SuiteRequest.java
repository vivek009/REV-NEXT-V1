package com.revnext.controller.catalog.request;


import lombok.Data;

import java.util.UUID;

@Data
public class SuiteRequest {
    private String name;
    private String description;
    private UUID formulaId;
    private UUID familyId;
    private String remark;
}
