package com.revnext.service.approval.handler;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.discount.Discount;
import com.revnext.repository.discount.DiscountRepository;
import com.revnext.service.approval.ApprovalActionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DiscountApprovalHandler implements ApprovalActionHandler {

    private final DiscountRepository discountRepository;

    @Override
    public String getEntityType() {
        return "DISCOUNT";
    }

    @Override
    public void handleApproval(UUID entityId, ApprovalStatus finalStatus) {
        Discount discount = discountRepository.findById(entityId)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found"));
        discount.setApprovalStatus(finalStatus);
        discountRepository.save(discount);
    }
}
