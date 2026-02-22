package com.revnext.service.catalog;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.approval.EntityStatus;
import com.revnext.domain.catalog.Product;
import com.revnext.repository.approval.EntityStatusRepository;
import com.revnext.repository.catalog.ProductRepository;
import com.revnext.service.BaseService;
import com.revnext.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService extends BaseService {

    private final ProductRepository productRepository;
    private final EntityStatusRepository entityStatusRepository;

    public ApprovalStatus getApprovalStatus(UUID entityId) {
        return entityStatusRepository.findByEntityIdAndEntityType(entityId, "PRODUCT")
                .map(EntityStatus::getStatus)
                .orElse(ApprovalStatus.DRAFT);
    }

    public java.util.Map<UUID, ApprovalStatus> getApprovalStatuses(java.util.Collection<UUID> entityIds) {
        if (entityIds == null || entityIds.isEmpty())
            return java.util.Collections.emptyMap();
        return entityStatusRepository.findByEntityIdInAndEntityType(entityIds, "PRODUCT")
                .stream()
                .collect(java.util.stream.Collectors.toMap(EntityStatus::getEntityId, EntityStatus::getStatus));
    }

    @Transactional
    public Product createProduct(Product product) {
        log.info("Creating new product: {}", product.getName());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public Product updateProduct(UUID id, Product updatedProduct) {
        log.info("Updating product with id: {}", id);
        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setSku(updatedProduct.getSku());
        existingProduct.setProductFamily(updatedProduct.getProductFamily());

        // Update attributes: Clear and re-add for simplicity in this implementation
        if (updatedProduct.getAttributes() != null) {
            existingProduct.getAttributes().clear();
            updatedProduct.getAttributes().forEach(attr -> {
                attr.setProduct(existingProduct);
                existingProduct.getAttributes().add(attr);
            });
        }

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        log.warn("Deleting product with id: {}", id);
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
