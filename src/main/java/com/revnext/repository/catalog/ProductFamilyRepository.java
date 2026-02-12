package com.revnext.repository.catalog;

import com.revnext.domain.catalog.ProductFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ProductFamilyRepository extends JpaRepository<ProductFamily, UUID> {
    Optional<ProductFamily> findByName(String name);
}
