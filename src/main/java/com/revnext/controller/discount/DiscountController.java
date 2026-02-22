package com.revnext.controller.discount;

import com.revnext.controller.BaseController;
import com.revnext.controller.discount.mapper.DiscountMapper;
import com.revnext.controller.discount.request.DiscountRequest;
import com.revnext.controller.discount.response.DiscountResponse;
import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.discount.Discount;
import com.revnext.repository.discount.DiscountRepository;
import com.revnext.service.discount.DiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final DiscountRepository discountRepository;

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
            return DiscountMapper.toResponse(saved, discountService.getApprovalStatus(saved.getId()));
        });
    }

    @GetMapping
    public ResponseEntity<Page<DiscountResponse>> getAllDiscounts(
            @RequestParam(required = false) ApprovalStatus status,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Fetching discounts status={}, q={}, page={}, size={}", status, q, page, size);
        Pageable pageable = PageRequest.of(page, size);
        return getResponse(() -> {
            if (q != null && !q.isBlank()) {
                return discountRepository.searchByText(q.trim(), pageable)
                        .map(d -> DiscountMapper.toResponse(d, discountService.getApprovalStatus(d.getId())));
            }
            if (status != null) {
                List<Discount> discounts = discountRepository.findByApprovalStatus(status);
                return new org.springframework.data.domain.PageImpl<>(
                        discounts.stream()
                                .map(d -> DiscountMapper.toResponse(d, status))
                                .collect(Collectors.toList()));
            }
            return discountRepository.findAll(pageable)
                    .map(d -> DiscountMapper.toResponse(d, discountService.getApprovalStatus(d.getId())));
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountResponse> getDiscountById(@PathVariable UUID id) {
        return getResponse(() -> {
            Discount discount = discountService.getDiscountById(id);
            return DiscountMapper.toResponse(discount, discountService.getApprovalStatus(discount.getId()));
        });
    }
}
