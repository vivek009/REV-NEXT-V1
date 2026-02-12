package com.revnext.controller.catalog;

import com.revnext.controller.catalog.mapper.SuiteMapper;
import com.revnext.controller.catalog.request.SuiteRequest;
import com.revnext.controller.catalog.response.SuiteResponse;
import com.revnext.domain.approval.ApprovalStatus;
import com.revnext.domain.catalog.Suite;
import com.revnext.repository.catalog.SuiteRepository;
import com.revnext.service.catalog.SuiteService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/suites")
@RequiredArgsConstructor
public class SuiteController {

    private final SuiteService suiteService;
    private final SuiteRepository suiteRepository;

    @PostMapping
    public ResponseEntity<SuiteResponse> create(@RequestBody SuiteRequest request) {
        log.info("API: Creating Suite name={}", request.getName());
        Suite suite = SuiteMapper.toEntity(request);
        Suite saved = suiteService.createSuite(suite);
        return ResponseEntity.ok(SuiteMapper.toResponse(saved, suiteService.getApprovalStatus(saved.getId())));
    }

    @GetMapping
    public ResponseEntity<List<SuiteResponse>> findAll(@RequestParam(required = false) ApprovalStatus status) {
        log.info("API: Fetching all Suites with status={}", status);
        List<Suite> suites = (status != null)
                ? suiteRepository.findByApprovalStatus(status)
                : suiteService.getAllSuites();
        return ResponseEntity.ok(
                suites.stream()
                        .map(s -> SuiteMapper.toResponse(s,
                                status != null ? status : suiteService.getApprovalStatus(s.getId())))
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuiteResponse> findById(@PathVariable UUID id) {
        log.info("API: Fetching Suite by id={}", id);
        Suite suite = suiteService.getSuiteById(id);
        return ResponseEntity.ok(SuiteMapper.toResponse(suite, suiteService.getApprovalStatus(suite.getId())));
    }

    // Fixed method call to use toResponse instead of ok if I changed it in my mind,
    // but SuiteMapper.toResponse is correct.
    // Wait, let me use the correct mapper method.

    @PutMapping("/{id}")
    public ResponseEntity<SuiteResponse> update(@PathVariable UUID id,
            @RequestBody SuiteRequest request) {
        log.info("API: Updating Suite id={}", id);
        Suite updated = SuiteMapper.toEntity(request);
        Suite saved = suiteService.updateSuite(id, updated);
        return ResponseEntity.ok(SuiteMapper.toResponse(saved, suiteService.getApprovalStatus(saved.getId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("API: Deleting Suite id={}", id);
        suiteService.deleteSuite(id);
        return ResponseEntity.noContent().build();
    }
}
