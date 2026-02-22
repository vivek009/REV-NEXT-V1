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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<SuiteResponse>> findAll(
            @RequestParam(required = false) ApprovalStatus status,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("API: Fetching all Suites with status={}, q={}, page={}, size={}", status, q, page, size);
        Pageable pageable = PageRequest.of(page, size);
        if (q != null && !q.isBlank()) {
            Page<SuiteResponse> results = suiteRepository.searchByText(q.trim(), pageable)
                    .map(s -> SuiteMapper.toResponse(s, suiteService.getApprovalStatus(s.getId())));
            return ResponseEntity.ok(results);
        }
        if (status != null) {
            List<Suite> suites = suiteRepository.findByApprovalStatus(status);
            return ResponseEntity.ok(new org.springframework.data.domain.PageImpl<>(
                    suites.stream()
                            .map(s -> SuiteMapper.toResponse(s, status))
                            .collect(Collectors.toList())));
        }
        return ResponseEntity.ok(
                suiteRepository.findAll(pageable)
                        .map(s -> SuiteMapper.toResponse(s, suiteService.getApprovalStatus(s.getId()))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuiteResponse> findById(@PathVariable UUID id) {
        log.info("API: Fetching Suite by id={}", id);
        Suite suite = suiteService.getSuiteById(id);
        return ResponseEntity.ok(SuiteMapper.toResponse(suite, suiteService.getApprovalStatus(suite.getId())));
    }

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
