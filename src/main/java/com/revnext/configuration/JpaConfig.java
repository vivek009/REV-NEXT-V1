package com.revnext.configuration;


import com.revnext.configuration.security.ApplicationUser;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
    @Bean
    public AuditorAware<UUID> auditorAware() {
        return new AuditorAwareImpl();
    }

    public static class AuditorAwareImpl implements AuditorAware<UUID> {
        @NotNull
        @Override
        public Optional<UUID> getCurrentAuditor() {
            return Optional.of(((ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        }
    }
}