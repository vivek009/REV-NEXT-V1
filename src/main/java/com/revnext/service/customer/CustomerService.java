package com.revnext.service.customer;

import com.revnext.domain.customer.Customer;
import com.revnext.domain.customer.CustomerSegment;
import com.revnext.repository.customer.CustomerRepository;
import com.revnext.repository.customer.CustomerSegmentRepository;
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
public class CustomerService extends BaseService {

    private final CustomerRepository customerRepository;
    private final CustomerSegmentRepository segmentRepository;

    // --- Customer CRUD ---
    @Transactional
    public Customer createCustomer(Customer customer) {
        log.info("Creating customer: {}", customer.getName());
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    @Transactional
    public Customer updateCustomer(UUID id, Customer updated) {
        Customer existing = getCustomerById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setAddress(updated.getAddress());
        return customerRepository.save(existing);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }

    // --- Segment CRUD ---
    @Transactional
    public CustomerSegment createSegment(CustomerSegment segment, List<UUID> customerIds) {
        log.info("Creating customer segment: {}", segment.getName());
        if (customerIds != null && !customerIds.isEmpty()) {
            List<Customer> customers = customerRepository.findAllById(customerIds);
            segment.setCustomers(customers);
        }
        return segmentRepository.save(segment);
    }

    public List<CustomerSegment> getAllSegments() {
        return segmentRepository.findAll();
    }

    public CustomerSegment getSegmentById(UUID id) {
        return segmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Segment not found with id: " + id));
    }

    @Transactional
    public CustomerSegment updateSegment(UUID id, CustomerSegment updated, List<UUID> customerIds) {
        CustomerSegment existing = getSegmentById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        if (customerIds != null) {
            List<Customer> customers = customerRepository.findAllById(customerIds);
            existing.setCustomers(customers);
        }
        return segmentRepository.save(existing);
    }

    @Transactional
    public void deleteSegment(UUID id) {
        segmentRepository.deleteById(id);
    }
}
