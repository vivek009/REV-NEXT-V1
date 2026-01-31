package com.revnext.controller.customer;

import com.revnext.controller.BaseController;
import com.revnext.controller.customer.mapper.CustomerMapper;
import com.revnext.controller.customer.mapper.CustomerSegmentMapper;
import com.revnext.controller.customer.request.CustomerRequest;
import com.revnext.controller.customer.request.CustomerSegmentRequest;
import com.revnext.controller.customer.response.CustomerResponse;
import com.revnext.controller.customer.response.CustomerSegmentResponse;
import com.revnext.domain.customer.Customer;
import com.revnext.domain.customer.CustomerSegment;
import com.revnext.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@Slf4j
@RequiredArgsConstructor
public class CustomerController extends BaseController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest request) {
        return getResponse(() -> {
            Customer customer = CustomerMapper.toEntity(request);
            Customer saved = customerService.createCustomer(customer);
            return CustomerMapper.toResponse(saved);
        });
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return getResponse(() -> {
            return customerService.getAllCustomers().stream()
                    .map(CustomerMapper::toResponse)
                    .collect(Collectors.toList());
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
        return getResponse(() -> {
            Customer customer = customerService.getCustomerById(id);
            return CustomerMapper.toResponse(customer);
        });
    }

    // --- Segments ---

    @PostMapping("/segments")
    public ResponseEntity<CustomerSegmentResponse> createSegment(@RequestBody CustomerSegmentRequest request) {
        return getResponse(() -> {
            CustomerSegment segment = CustomerSegmentMapper.toEntity(request);
            CustomerSegment saved = customerService.createSegment(segment, request.getCustomerIds());
            return CustomerSegmentMapper.toResponse(saved);
        });
    }

    @GetMapping("/segments")
    public ResponseEntity<List<CustomerSegmentResponse>> getAllSegments() {
        return getResponse(() -> {
            return customerService.getAllSegments().stream()
                    .map(CustomerSegmentMapper::toResponse)
                    .collect(Collectors.toList());
        });
    }
}
