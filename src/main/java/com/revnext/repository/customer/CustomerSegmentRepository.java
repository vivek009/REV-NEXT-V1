package com.revnext.repository.customer;

import com.revnext.domain.customer.CustomerSegment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerSegmentRepository extends JpaRepository<CustomerSegment , UUID> {
}
