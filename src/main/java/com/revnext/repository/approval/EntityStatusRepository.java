package com.revnext.repository.approval;

import com.revnext.domain.approval.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntityStatusRepository extends JpaRepository<EntityStatus, UUID> {
    Optional<EntityStatus> findByEntityIdAndEntityType(UUID entityId, String entityType);

    List<EntityStatus> findByEntityIdInAndEntityType(Collection<UUID> entityIds, String entityType);
}
