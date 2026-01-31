package com.revnext.service.approval.handler;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.catalog.Product;
import com.revnext.repository.catalog.ProductRepository;
import com.revnext.service.approval.ApprovalActionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductApprovalHandler implements ApprovalActionHandler {

    private final ProductRepository productRepository;

    @Override
    public String getEntityType() {
        return "PRODUCT";
    }

    @Override
    public void handleApproval(UUID entityId, ApprovalStatus finalStatus) {
        Product product = productRepository.findById(entityId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        product.setApprovalStatus(finalStatus);
        productRepository.save(product);
    }
}
