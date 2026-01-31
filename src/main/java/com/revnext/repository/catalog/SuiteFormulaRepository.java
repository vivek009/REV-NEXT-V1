package com.revnext.repository.catalog;

import com.revnext.domain.catalog.SuiteFormula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SuiteFormulaRepository extends JpaRepository<SuiteFormula, UUID> {

}
