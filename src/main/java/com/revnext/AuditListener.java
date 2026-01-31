package com.revnext;

import com.revnext.domain.BaseData;
import jakarta.persistence.PrePersist;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuditListener {
    private final AuditorAware<UUID> auditorAware;

    public AuditListener(AuditorAware<UUID> auditorAware) {
        this.auditorAware = auditorAware;
    }

    @PrePersist
    public void setCreatedByIfNull(BaseData entity) {
        if (entity.getCreatedBy() == null) {
            auditorAware.getCurrentAuditor().ifPresent(entity::setCreatedBy);
        }
    }
}

