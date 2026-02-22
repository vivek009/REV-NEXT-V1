package com.revnext.controller.catalog;

import com.revnext.controller.BaseController;
import com.revnext.controller.catalog.mapper.ProductMapper;
import com.revnext.controller.catalog.request.ProductRequest;
import com.revnext.controller.catalog.response.ProductResponse;
import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.catalog.Product;
import com.revnext.repository.catalog.ProductRepository;
import com.revnext.service.catalog.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@Slf4j
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return getResponse(() -> {
            Product product = ProductMapper.toEntity(request);
            Product saved = productService.createProduct(product);
            return ProductMapper.toResponse(saved, productService.getApprovalStatus(saved.getId()));
        });
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(required = false) ApprovalStatus status,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Received request to fetch products with status: {}, q: {}, page: {}, size: {}", status, q, page,
                size);
        Pageable pageable = PageRequest.of(page, size);
        return getResponse(() -> {
            Page<Product> productPage;
            if (q != null && !q.isBlank()) {
                productPage = productRepository.searchByText(q.trim(), pageable);
            } else if (status != null) {
                List<Product> products = productRepository.findByApprovalStatus(status);
                return new org.springframework.data.domain.PageImpl<>(
                        products.stream()
                                .map(p -> ProductMapper.toResponse(p, status))
                                .collect(Collectors.toList()));
            } else {
                productPage = productRepository.findAll(pageable);
            }
            // Batch-load approval statuses in a single query
            Map<UUID, ApprovalStatus> statusMap = productService.getApprovalStatuses(
                    productPage.getContent().stream().map(Product::getId).collect(Collectors.toList()));
            return productPage.map(p -> ProductMapper.toResponse(p,
                    statusMap.getOrDefault(p.getId(), ApprovalStatus.DRAFT)));
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        log.info("Received request to fetch product: {}", id);
        return getResponse(() -> {
            Product product = productService.getProductById(id);
            return ProductMapper.toResponse(product, productService.getApprovalStatus(product.getId()));
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID id, @RequestBody ProductRequest request) {
        return getResponse(() -> {
            Product product = ProductMapper.toEntity(request);
            Product updated = productService.updateProduct(id, product);
            return ProductMapper.toResponse(updated, productService.getApprovalStatus(updated.getId()));
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        return getResponse(() -> {
            productService.deleteProduct(id);
            return null;
        });
    }
}
