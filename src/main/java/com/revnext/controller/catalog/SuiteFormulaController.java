package com.revnext.controller.catalog;

import com.revnext.controller.catalog.mapper.SuiteFormulaMapper;
import com.revnext.controller.catalog.request.SuiteFormulaRequest;
import com.revnext.controller.catalog.response.SuiteFormulaResponse;
import com.revnext.domain.catalog.SuiteFormula;
import com.revnext.service.catalog.SuiteFormulaService;
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
@RequestMapping("/api/suite-formulas")
@RequiredArgsConstructor
public class SuiteFormulaController {

    private final SuiteFormulaService service;

    @PostMapping
    public ResponseEntity<SuiteFormulaResponse> create(@RequestBody SuiteFormulaRequest request) {
        log.info("API: Creating SuiteFormula");
        SuiteFormula formula = SuiteFormulaMapper.toEntity(request);
        SuiteFormula saved = service.create(formula);
        return ResponseEntity.ok(SuiteFormulaMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<SuiteFormulaResponse>> findAll() {
        log.info("API: Fetching all SuiteFormulas");
        return ResponseEntity.ok(
                service.findAll().stream()
                        .map(SuiteFormulaMapper::toResponse)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuiteFormulaResponse> findById(@PathVariable UUID id) {
        log.info("API: Fetching SuiteFormula by id={}", id);
        return ResponseEntity.ok(SuiteFormulaMapper.toResponse(service.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuiteFormulaResponse> update(@PathVariable UUID id,
            @RequestBody SuiteFormulaRequest request) {
        log.info("API: Updating SuiteFormula id={}", id);
        SuiteFormula updatedFormula = SuiteFormulaMapper.toEntity(request);
        SuiteFormula saved = service.update(id, updatedFormula);
        return ResponseEntity.ok(SuiteFormulaMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("API: Deleting SuiteFormula id={}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
