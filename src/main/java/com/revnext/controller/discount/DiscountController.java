package com.revnext.controller.discount;

import com.revnext.controller.BaseController;
import com.revnext.controller.discount.mapper.DiscountMapper;
import com.revnext.controller.discount.request.DiscountRequest;
import com.revnext.controller.discount.response.DiscountResponse;
import com.revnext.domain.discount.Discount;
import com.revnext.service.discount.DiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/discounts")
@Slf4j
@RequiredArgsConstructor
public class DiscountController extends BaseController {

    private final DiscountService discountService;

    @PostMapping
    public ResponseEntity<DiscountResponse> createDiscount(@RequestBody DiscountRequest request) {
        return getResponse(() -> {
            Discount discount = DiscountMapper.toEntity(request);
            Discount saved = discountService.createDiscount(
                    discount,
                    request.getCustomerId(),
                    request.getSegmentId(),
                    request.getProductId(),
                    request.getSuiteId());
            return DiscountMapper.toResponse(saved);
        });
    }

    @GetMapping
    public ResponseEntity<List<DiscountResponse>> getAllDiscounts() {
        return getResponse(() -> {
            return discountService.getAllDiscounts().stream()
                    .map(DiscountMapper::toResponse)
                    .collect(Collectors.toList());
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountResponse> getDiscountById(@PathVariable UUID id) {
        return getResponse(() -> {
            Discount discount = discountService.getDiscountById(id);
            return DiscountMapper.toResponse(discount);
        });
    }
}
