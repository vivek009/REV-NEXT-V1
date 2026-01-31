package com.revnext.service.discount;

import com.revnext.domain.discount.CustomerDiscount;
import com.revnext.domain.discount.CustomerSegmentDiscount;
import com.revnext.domain.discount.Discount;
import com.revnext.domain.discount.SuiteDiscount;
import com.revnext.repository.customer.CustomerRepository;
import com.revnext.repository.customer.CustomerSegmentRepository;
import com.revnext.repository.catalog.ProductRepository;
import com.revnext.repository.catalog.SuiteRepository;
import com.revnext.repository.discount.CustomerDiscountRepository;
import com.revnext.repository.discount.CustomerSegmentDiscountRepository;
import com.revnext.repository.discount.DiscountRepository;
import com.revnext.repository.discount.SuiteDiscountRepository;
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
public class DiscountService extends BaseService {

    private final DiscountRepository discountRepository;
    private final CustomerDiscountRepository customerDiscountRepository;
    private final CustomerSegmentDiscountRepository segmentDiscountRepository;
    private final SuiteDiscountRepository suiteDiscountRepository;

    private final CustomerRepository customerRepository;
    private final CustomerSegmentRepository segmentRepository;
    private final ProductRepository productRepository;
    private final SuiteRepository suiteRepository;

    @Transactional
    public Discount createDiscount(Discount discount, UUID customerId, UUID segmentId, UUID productId, UUID suiteId) {
        log.info("Creating discount: {}", discount.getDiscountName());
        Discount savedDiscount = discountRepository.save(discount);

        if (customerId != null) {
            CustomerDiscount cd = CustomerDiscount.builder()
                    .discount(savedDiscount)
                    .customer(customerRepository.findById(customerId).orElse(null))
                    .product(productId != null ? productRepository.findById(productId).orElse(null) : null)
                    .suite(suiteId != null ? suiteRepository.findById(suiteId).orElse(null) : null)
                    .build();
            customerDiscountRepository.save(cd);
        } else if (segmentId != null) {
            CustomerSegmentDiscount csd = CustomerSegmentDiscount.builder()
                    .discount(savedDiscount)
                    .customerSegment(segmentRepository.findById(segmentId).orElse(null))
                    .product(productId != null ? productRepository.findById(productId).orElse(null) : null)
                    .suite(suiteId != null ? suiteRepository.findById(suiteId).orElse(null) : null)
                    .build();
            segmentDiscountRepository.save(csd);
        } else if (suiteId != null) {
            SuiteDiscount sd = SuiteDiscount.builder()
                    .discount(savedDiscount)
                    .suite(suiteRepository.findById(suiteId).orElse(null))
                    .build();
            suiteDiscountRepository.save(sd);
        }

        return savedDiscount;
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountById(UUID id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount not found with id: " + id));
    }
}
