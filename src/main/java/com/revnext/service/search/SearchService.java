package com.revnext.service.search;

import com.revnext.controller.search.response.GlobalSearchResponse;
import com.revnext.repository.catalog.ProductRepository;
import com.revnext.repository.catalog.SuiteRepository;
import com.revnext.repository.customer.CustomerRepository;
import com.revnext.repository.discount.DiscountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

    private final ProductRepository productRepository;
    private final SuiteRepository suiteRepository;
    private final CustomerRepository customerRepository;
    private final DiscountRepository discountRepository;

    public GlobalSearchResponse globalSearch(String query, int page, int size) {
        log.info("Global search for query='{}' page={} size={}", query, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return GlobalSearchResponse.builder()
                .products(productRepository.searchByText(query, pageable))
                .suites(suiteRepository.searchByText(query, pageable))
                .customers(customerRepository.searchByText(query, pageable))
                .discounts(discountRepository.searchByText(query, pageable))
                .build();
    }
}
