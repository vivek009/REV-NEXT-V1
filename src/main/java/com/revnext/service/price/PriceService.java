package com.revnext.service.price;

import com.revnext.domain.price.Price;
import com.revnext.domain.price.PriceValidity;
import com.revnext.domain.price.TieredPrice;
import com.revnext.repository.price.PriceRepository;
import com.revnext.repository.price.PriceValidityRepository;
import com.revnext.repository.price.TieredPriceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional // ✅ write methods transactional by default
public class PriceService {

    private final PriceRepository priceRepository;
    private final PriceValidityRepository priceValidityRepository;
    private final TieredPriceRepository tieredPriceRepository;

    // --- Price ---
    public Price createPrice(Price price) {
        log.info("Creating new Price: {}", price.getName());
        return priceRepository.save(price);
    }

    @Transactional(readOnly = true)
    public Optional<Price> getPrice(UUID id) {
        log.debug("Fetching Price id={}", id);
        return priceRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Price> getAllPrices() {
        log.debug("Fetching all Prices");
        return priceRepository.findAll();
    }

    public void deletePrice(UUID id) {
        log.warn("Deleting Price id={}", id);
        priceRepository.deleteById(id);
    }

    // --- Validities ---
    @Transactional(readOnly = true)
    public List<PriceValidity> getValiditiesByPrice(UUID priceId) {
        log.debug("Fetching PriceValidities for priceId={}", priceId);
        return priceValidityRepository.findByPriceId(priceId);
    }

    public PriceValidity createValidity(PriceValidity validity) {
        log.info("Creating PriceValidity for price id={}", validity.getPrice().getId());
        return priceValidityRepository.save(validity);
    }

    @Transactional(readOnly = true)
    public Optional<PriceValidity> getValidity(UUID validityId) {
        log.debug("Fetching PriceValidity id={}", validityId);
        return priceValidityRepository.findById(validityId);
    }

    // --- Tiers ---
    public TieredPrice createTier(UUID validityId, TieredPrice tier) {
        log.info("Creating Tier for validityId={}", validityId);
        PriceValidity validity = priceValidityRepository.findById(validityId)
                .orElseThrow(() -> new IllegalArgumentException("PriceValidity not found"));

        validity.addTier(tier); // keep bidirectional association consistent
        priceValidityRepository.save(validity);

        log.debug("Created TieredPrice: {}", tier);
        return tier;
    }

    @Transactional(readOnly = true)
    public List<TieredPrice> getTiersByValidity(UUID validityId) {
        log.debug("Fetching Tiers for validityId={}", validityId);
        return tieredPriceRepository.findByPriceValidityId(validityId);
    }

    // --- Business Logic ---
    @Transactional(readOnly = true)
    public Optional<BigDecimal> getApplicablePrice(UUID productId, Date date, Integer quantity) {
        log.debug("Calculating applicable price for productId={}, date={}, qty={}", productId, date, quantity);
        List<PriceValidity> validities = priceValidityRepository.findAll();

        for (PriceValidity validity : validities) {
            if (validity.getPrice().getProduct().getId().equals(productId)
                    && date.after(validity.getValidFrom())
                    && date.before(validity.getValidTill())) {

                for (TieredPrice tier : validity.getTiers()) {
                    boolean withinRange =
                            quantity >= tier.getMinQuantity() &&
                                    (tier.getMaxQuantity() == null || quantity <= tier.getMaxQuantity());

                    if (withinRange) {
                        log.info("Applicable price found from tier id={}: {}", tier.getId(), tier.getUnitPrice());
                        return Optional.of(tier.getUnitPrice());
                    }
                }

                log.info("Falling back to targetPrice={}", validity.getPrice().getTargetPrice());
                return Optional.of(validity.getPrice().getTargetPrice());
            }
        }
        log.warn("No applicable price found for productId={}, date={}, qty={}", productId, date, quantity);
        return Optional.empty();
    }

    public Price updatePrice(UUID id, Price updated) {
        log.info("Updating Price id={}", id);
        Price existing = priceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Price not found"));

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setCeilPrice(updated.getCeilPrice());
        existing.setFloorPrice(updated.getFloorPrice());
        existing.setTargetPrice(updated.getTargetPrice());

        Price saved = priceRepository.save(existing);
        log.debug("Updated Price: {}", saved);
        return saved;
    }

    public PriceValidity updateValidity(UUID id, PriceValidity updated) {
        log.info("Updating PriceValidity id={}", id);
        PriceValidity existing = priceValidityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PriceValidity not found"));

        existing.setLocation(updated.getLocation());
        existing.setCurrency(updated.getCurrency());
        existing.setCountry(updated.getCountry());
        existing.setValidFrom(updated.getValidFrom());
        existing.setValidTill(updated.getValidTill());

        PriceValidity saved = priceValidityRepository.save(existing);
        log.debug("Updated PriceValidity: {}", saved);
        return saved;
    }

    public TieredPrice updateTier(UUID id, TieredPrice updated) {
        log.info("Updating TieredPrice id={}", id);
        TieredPrice existing = tieredPriceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TieredPrice not found"));

        existing.setMinQuantity(updated.getMinQuantity());
        existing.setMaxQuantity(updated.getMaxQuantity());
        existing.setUnitPrice(updated.getUnitPrice());

        TieredPrice saved = tieredPriceRepository.save(existing);
        log.debug("Updated TieredPrice: {}", saved);
        return saved;
    }
}
