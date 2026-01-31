package com.revnext.controller.catalog;


import com.revnext.controller.catalog.mapper.SuiteMapper;
import com.revnext.controller.catalog.request.SuiteRequest;
import com.revnext.controller.catalog.response.SuiteResponse;
import com.revnext.domain.catalog.Suite;
import com.revnext.service.catalog.SuiteService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/suites")
@RequiredArgsConstructor
public class SuiteController {

    private final SuiteService service;

    @PostMapping
    public ResponseEntity<SuiteResponse> create(@RequestBody SuiteRequest request) {
        log.info("API: Creating Suite name={}", request.getName());
        Suite suite = SuiteMapper.toEntity(request);
        Suite saved = service.create(suite);
        return ResponseEntity.ok(SuiteMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<SuiteResponse>> findAll() {
        log.info("API: Fetching all Suites");
        return ResponseEntity.ok(
                service.findAll().stream()
                        .map(SuiteMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuiteResponse> findById(@PathVariable UUID id) {
        log.info("API: Fetching Suite by id={}", id);
        return ResponseEntity.ok(SuiteMapper.toResponse(service.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuiteResponse> update(@PathVariable UUID id,
                                                @RequestBody SuiteRequest request) {
        log.info("API: Updating Suite id={}", id);
        Suite updated = SuiteMapper.toEntity(request);
        Suite saved = service.update(id, updated);
        return ResponseEntity.ok(SuiteMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("API: Deleting Suite id={}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
