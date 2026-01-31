package com.revnext.repository.config;

import com.revnext.domain.config.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ConfigRepository extends JpaRepository<Config, UUID> {
    Optional<Config> findByConfigurationName(String configName);

    @Query("SELECT c FROM Config c " +
            "WHERE :validFrom >= c.validFrom " +
            "AND :validTill <= c.validTill")
    Optional<Config> findMatchingConfig(@Param("validFrom") LocalDate validFrom,
                                        @Param("validTill") LocalDate validTill);
}
