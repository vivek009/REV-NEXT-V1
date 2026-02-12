package com.revnext.controller.catalog.mapper;

import com.revnext.controller.catalog.request.SuiteRequest;
import com.revnext.controller.catalog.response.SuiteResponse;
import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.catalog.ProductFamily;
import com.revnext.domain.catalog.Suite;
import com.revnext.domain.catalog.SuiteFormula;

public class SuiteMapper {

    public static Suite toEntity(SuiteRequest request) {
        Suite suite = new Suite();
        suite.setName(request.getName());
        suite.setDescription(request.getDescription());
        suite.setRemark(request.getRemark());

        if (request.getFormulaId() != null) {
            SuiteFormula formula = new SuiteFormula();
            formula.setId(request.getFormulaId());
            suite.setFormula(formula);
        }

        if (request.getFamilyId() != null) {
            ProductFamily family = new ProductFamily();
            family.setId(request.getFamilyId());
            suite.setFamily(family);
        }

        return suite;
    }

    public static SuiteResponse toResponse(Suite suite, ApprovalStatus status) {
        return SuiteResponse.builder()
                .id(suite.getId())
                .name(suite.getName())
                .description(suite.getDescription())
                .remark(suite.getRemark())
                .formulaId(suite.getFormula() != null ? suite.getFormula().getId() : null)
                .familyId(suite.getFamily() != null ? suite.getFamily().getId() : null)
                .approvalStatus(status)
                .build();
    }
}
