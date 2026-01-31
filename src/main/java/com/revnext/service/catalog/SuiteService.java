package com.revnext.service.catalog;


import com.revnext.domain.catalog.Suite;
import com.revnext.repository.catalog.SuiteRepository;
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
public class SuiteService {

    private final SuiteRepository repository;

    public Suite create(Suite suite) {
        log.info("Creating new Suite with name={}", suite.getName());
        Suite saved = repository.save(suite);
        log.debug("Created Suite: {}", saved);
        return saved;
    }

    public List<Suite> findAll() {
        log.info("Fetching all Suites");
        return repository.findAll();
    }

    public Suite findById(UUID id) {
        log.info("Fetching Suite with id={}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Suite not found with id={}", id);
                    return new IllegalArgumentException("Suite not found");
                });
    }

    public Suite update(UUID id, Suite updated) {
        log.info("Updating Suite with id={}", id);
        Suite existing = findById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setRemark(updated.getRemark());
        existing.setFormula(updated.getFormula());
        existing.setFamily(updated.getFamily());

        Suite saved = repository.save(existing);
        log.debug("Updated Suite: {}", saved);
        return saved;
    }

    public void delete(UUID id) {
        log.warn("Deleting Suite with id={}", id);
        repository.deleteById(id);
    }
}
