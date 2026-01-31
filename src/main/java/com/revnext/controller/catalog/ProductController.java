package com.revnext.controller.catalog;

import com.revnext.controller.BaseController;
import com.revnext.controller.catalog.mapper.ProductMapper;
import com.revnext.controller.catalog.request.ProductRequest;
import com.revnext.controller.catalog.response.ProductResponse;
import com.revnext.domain.catalog.Product;
import com.revnext.service.catalog.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/products")
@Slf4j
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        log.info("Received request to create product: {}", request.getName());
        return getResponse(() -> {
            Product product = ProductMapper.toEntity(request);
            Product saved = productService.createProduct(product);
            return ProductMapper.toResponse(saved);
        });
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("Received request to fetch all products");
        return getResponse(() -> {
            List<Product> products = productService.getAllProducts();
            return products.stream()
                    .map(ProductMapper::toResponse)
                    .collect(Collectors.toList());
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        log.info("Received request to fetch product: {}", id);
        return getResponse(() -> {
            Product product = productService.getProductById(id);
            return ProductMapper.toResponse(product);
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID id, @RequestBody ProductRequest request) {
        log.info("Received request to update product: {}", id);
        return getResponse(() -> {
            Product product = ProductMapper.toEntity(request);
            Product updated = productService.updateProduct(id, product);
            return ProductMapper.toResponse(updated);
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID id) {
        log.info("Received request to delete product: {}", id);
        return getResponse(() -> {
            productService.deleteProduct(id);
            return "Product deleted successfully";
        });
    }
}
