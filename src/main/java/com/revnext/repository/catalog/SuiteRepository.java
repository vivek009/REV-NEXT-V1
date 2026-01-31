package com.revnext.repository.catalog;

import com.revnext.domain.catalog.Suite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SuiteRepository extends JpaRepository<Suite, UUID> {
}
