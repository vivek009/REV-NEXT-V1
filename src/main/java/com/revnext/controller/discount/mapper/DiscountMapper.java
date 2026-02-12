package com.revnext.controller.discount.mapper;

import com.revnext.controller.discount.request.DiscountRequest;
import com.revnext.controller.discount.response.DiscountResponse;
import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.discount.Discount;

public class DiscountMapper {

    public static Discount toEntity(DiscountRequest request) {
        return Discount.builder()
                .discountName(request.getName())
                .discountRule(request.getRule())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }

    public static DiscountResponse toResponse(Discount discount, ApprovalStatus status) {
        return DiscountResponse.builder()
                .id(discount.getId())
                .name(discount.getDiscountName())
                .rule(discount.getDiscountRule())
                .startDate(discount.getStartDate())
                .endDate(discount.getEndDate())
                .approvalStatus(status)
                .build();
    }
}
