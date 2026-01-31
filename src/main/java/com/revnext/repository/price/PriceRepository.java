package com.revnext.repository.price;

import com.revnext.domain.price.Price;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, UUID> {

    Optional<Price> findByName(String name);

    List<Price> findByProductId(UUID productId);
}
