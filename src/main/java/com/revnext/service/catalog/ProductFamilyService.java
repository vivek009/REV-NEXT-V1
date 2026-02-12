package com.revnext.service.catalog;

import com.revnext.domain.catalog.ProductFamily;
import com.revnext.repository.catalog.ProductFamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductFamilyService {
    private final ProductFamilyRepository repository;

    public ProductFamily createFamily(ProductFamily family) {
        return repository.save(family);
    }

    public List<ProductFamily> getAllFamilies() {
        return repository.findAll();
    }

    public Optional<ProductFamily> findByName(String name) {
        return repository.findByName(name);
    }
}
