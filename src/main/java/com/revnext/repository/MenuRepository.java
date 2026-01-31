package com.revnext.repository;

import com.revnext.constants.Roles;
import com.revnext.domain.menu.Menu;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {

    @Query("SELECT DISTINCT m FROM Menu m JOIN m.roles r WHERE r.name = :roleName AND m.disabled = false ORDER BY m.order ASC")
    List<Menu> findMenusByRoleName(@Param("roleName") Roles roleName);

    @Query(" SELECT m FROM Menu m JOIN m.roles r WHERE r.name = :roleName AND m.id = :menuId AND m.disabled = false")
    Optional<Menu> getMenuForRoleAndId(@Param("menuId") UUID menuId, @Param("roleName") Roles roleName);


}
