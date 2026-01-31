package com.revnext.service.catalog;

import com.revnext.domain.catalog.SuiteFormula;
import com.revnext.repository.catalog.SuiteFormulaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SuiteFormulaService {

    private final SuiteFormulaRepository repository;

    public SuiteFormula create(SuiteFormula formula) {
        log.info("Creating new SuiteFormula for familyId={}",
                formula.getFamily() != null ? formula.getFamily().getId() : null);
        SuiteFormula saved = repository.save(formula);
        log.debug("Created SuiteFormula: {}", saved);
        return saved;
    }

    public List<SuiteFormula> findAll() {
        log.info("Fetching all SuiteFormulas");
        return repository.findAll();
    }

    public SuiteFormula findById(UUID id) {
        log.info("Fetching SuiteFormula with id={}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("SuiteFormula not found with id={}", id);
                    return new IllegalArgumentException("Formula not found");
                });
    }

    public SuiteFormula update(UUID id, SuiteFormula updated) {
        log.info("Updating SuiteFormula with id={}", id);
        SuiteFormula existing = findById(id);
        existing.setFamily(updated.getFamily());

        existing.getIngredients().clear();
        updated.getIngredients().forEach(existing::addIngredient);

        SuiteFormula saved = repository.save(existing);
        log.debug("Updated SuiteFormula: {}", saved);
        return saved;
    }

    public void delete(UUID id) {
        log.warn("Deleting SuiteFormula with id={}", id);
        repository.deleteById(id);
    }
}
