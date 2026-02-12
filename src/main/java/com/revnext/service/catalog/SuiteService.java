package com.revnext.service.catalog;

import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.approval.EntityStatus;
import com.revnext.domain.catalog.Suite;
import com.revnext.repository.approval.EntityStatusRepository;
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

    private final SuiteRepository suiteRepository;
    private final EntityStatusRepository entityStatusRepository;

    public ApprovalStatus getApprovalStatus(UUID entityId) {
        return entityStatusRepository.findByEntityIdAndEntityType(entityId, "SUITE")
                .map(EntityStatus::getStatus)
                .orElse(ApprovalStatus.DRAFT);
    }

    public Suite createSuite(Suite suite) {
        log.info("Creating new Suite with name={}", suite.getName());
        Suite saved = suiteRepository.save(suite);
        log.debug("Created Suite: {}", saved);
        return saved;
    }

    public List<Suite> getAllSuites() {
        log.info("Fetching all Suites");
        return suiteRepository.findAll();
    }

    public Suite getSuiteById(UUID id) {
        log.info("Fetching Suite with id={}", id);
        return suiteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Suite not found with id={}", id);
                    return new IllegalArgumentException("Suite not found");
                });
    }

    public Suite updateSuite(UUID id, Suite updated) {
        log.info("Updating Suite with id={}", id);
        Suite existing = getSuiteById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setRemark(updated.getRemark());
        existing.setFormula(updated.getFormula());
        existing.setFamily(updated.getFamily());

        Suite saved = suiteRepository.save(existing);
        log.debug("Updated Suite: {}", saved);
        return saved;
    }

    public void deleteSuite(UUID id) {
        log.warn("Deleting Suite with id={}", id);
        suiteRepository.deleteById(id);
    }
}
