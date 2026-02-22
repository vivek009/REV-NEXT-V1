package com.revnext.repository.customer;

import com.revnext.domain.customer.Customer;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query(value = "SELECT * FROM CUSTOMER WHERE to_tsvector('english', coalesce(name,'') || ' ' || coalesce(description,'') || ' ' || coalesce(address,'')) @@ plainto_tsquery('english', :query)", countQuery = "SELECT count(*) FROM CUSTOMER WHERE to_tsvector('english', coalesce(name,'') || ' ' || coalesce(description,'') || ' ' || coalesce(address,'')) @@ plainto_tsquery('english', :query)", nativeQuery = true)
    Page<Customer> searchByText(@Param("query") String query, Pageable pageable);
}
