package com.revnext.controller.catalog;

import com.revnext.controller.BaseController;
import com.revnext.domain.catalog.ProductFamily;
import com.revnext.service.catalog.ProductFamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/product-families")
@RequiredArgsConstructor
public class ProductFamilyController extends BaseController {
    private final ProductFamilyService service;

    @PostMapping
    public ResponseEntity<ProductFamily> createFamily(@RequestBody ProductFamily family) {
        return getResponse(() -> service.createFamily(family));
    }

    @GetMapping
    public ResponseEntity<List<ProductFamily>> getAllFamilies() {
        return getResponse(service::getAllFamilies);
    }
}
