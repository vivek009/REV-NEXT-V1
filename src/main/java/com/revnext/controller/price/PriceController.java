package com.revnext.controller.price;

import com.revnext.controller.price.mapper.PriceMapper;
import com.revnext.controller.price.mapper.PriceValidityMapper;
import com.revnext.controller.price.mapper.TieredPriceMapper;
import com.revnext.controller.price.request.PriceRequest;
import com.revnext.controller.price.request.PriceValidityRequest;
import com.revnext.controller.price.request.TieredPriceRequest;
import com.revnext.controller.price.response.PriceResponse;
import com.revnext.controller.price.response.PriceValidityResponse;
import com.revnext.controller.price.response.TieredPriceResponse;
import com.revnext.domain.price.Price;
import com.revnext.domain.price.PriceValidity;
import com.revnext.domain.price.TieredPrice;
import com.revnext.service.price.PriceService;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    // --- Price ---
    @PostMapping
    public ResponseEntity<PriceResponse> createPrice(@RequestBody PriceRequest request) {
        log.info("API: Creating Price with name={}", request.getName());
        Price saved = priceService.createPrice(PriceMapper.toEntity(request));
        log.debug("Created Price: {}", saved);
        return ResponseEntity.ok(PriceMapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceResponse> getPrice(@PathVariable UUID id) {
        log.info("API: Fetching Price id={}", id);
        Price price = priceService.getPrice(id)
                .orElseThrow(() -> new IllegalArgumentException("Price not found"));
        log.debug("Fetched Price: {}", price);
        return ResponseEntity.ok(PriceMapper.toResponse(price));
    }

    @GetMapping
    public ResponseEntity<List<PriceResponse>> getAllPrices() {
        log.info("API: Fetching all Prices");
        return ResponseEntity.ok(
                priceService.getAllPrices().stream()
                        .map(PriceMapper::toResponse)
                        .collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable UUID id) {
        log.warn("API: Deleting Price id={}", id);
        priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }

    // --- Validities ---
    @PostMapping("/{priceId}/validities")
    public ResponseEntity<PriceValidityResponse> createValidity(
            @PathVariable UUID priceId, @RequestBody PriceValidityRequest request) {

        log.info("API: Creating PriceValidity for priceId={}", priceId);
        Price price = priceService.getPrice(priceId)
                .orElseThrow(() -> new IllegalArgumentException("Price not found"));

        PriceValidity saved = priceService.createValidity(
                PriceValidityMapper.toEntity(request, price));
        log.debug("Created PriceValidity: {}", saved);
        return ResponseEntity.ok(PriceValidityMapper.toResponse(saved));
    }

    @GetMapping("/{priceId}/validities")
    public ResponseEntity<List<PriceValidityResponse>> getValidities(@PathVariable UUID priceId) {
        log.info("API: Fetching PriceValidities for priceId={}", priceId);
        return ResponseEntity.ok(
                priceService.getValiditiesByPrice(priceId).stream()
                        .map(PriceValidityMapper::toResponse)
                        .collect(Collectors.toList()));
    }

    // --- Tiers ---
    @PostMapping("/validities/{validityId}/tiers")
    public ResponseEntity<TieredPriceResponse> createTier(
            @PathVariable UUID validityId, @RequestBody TieredPriceRequest request) {

        log.info("API: Creating Tier for validityId={}", validityId);
        TieredPrice saved = priceService.createTier(
                validityId, TieredPriceMapper.toEntity(request));
        log.debug("Created TieredPrice: {}", saved);
        return ResponseEntity.ok(TieredPriceMapper.toResponse(saved));
    }

    @GetMapping("/validities/{validityId}/tiers")
    public ResponseEntity<List<TieredPriceResponse>> getTiers(@PathVariable UUID validityId) {
        log.info("API: Fetching Tiers for validityId={}", validityId);
        return ResponseEntity.ok(
                priceService.getTiersByValidity(validityId).stream()
                        .map(TieredPriceMapper::toResponse)
                        .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceResponse> updatePrice(
            @PathVariable UUID id, @RequestBody PriceRequest request) {
        log.info("API: Updating Price id={}", id);
        Price updated = priceService.updatePrice(id, PriceMapper.toEntity(request));
        log.debug("Updated Price: {}", updated);
        return ResponseEntity.ok(PriceMapper.toResponse(updated));
    }

    @PutMapping("/validities/{id}")
    public ResponseEntity<PriceValidityResponse> updateValidity(
            @PathVariable UUID id, @RequestBody PriceValidityRequest request) {
        log.info("API: Updating PriceValidity id={}", id);
        PriceValidity updated = priceService.updateValidity(
                id, PriceValidityMapper.toEntity(request, null));
        log.debug("Updated PriceValidity: {}", updated);
        return ResponseEntity.ok(PriceValidityMapper.toResponse(updated));
    }

    @PutMapping("/tiers/{id}")
    public ResponseEntity<TieredPriceResponse> updateTier(
            @PathVariable UUID id, @RequestBody TieredPriceRequest request) {
        log.info("API: Updating TieredPrice id={}", id);
        TieredPrice updated = priceService.updateTier(
                id, TieredPriceMapper.toEntity(request));
        log.debug("Updated TieredPrice: {}", updated);
        return ResponseEntity.ok(TieredPriceMapper.toResponse(updated));
    }
}
