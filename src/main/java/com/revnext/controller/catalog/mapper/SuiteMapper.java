package com.revnext.controller.catalog.mapper;



import com.revnext.controller.catalog.request.SuiteRequest;
import com.revnext.controller.catalog.response.SuiteResponse;
import com.revnext.domain.catalog.Suite;


public class SuiteMapper {

    public static Suite toEntity(SuiteRequest request) {
        Suite suite = new Suite();
        suite.setName(request.getName());
        suite.setDescription(request.getDescription());
        suite.setRemark(request.getRemark());
        return suite;
    }

    public static SuiteResponse toResponse(Suite suite) {
        SuiteResponse response = new SuiteResponse();
        response.setId(suite.getId());
        response.setName(suite.getName());
        response.setDescription(suite.getDescription());
        response.setRemark(suite.getRemark());
        response.setFormulaId(suite.getFormula() != null ? suite.getFormula().getId() : null);
        response.setFamilyId(suite.getFamily() != null ? suite.getFamily().getId() : null);
        return response;
    }
}
