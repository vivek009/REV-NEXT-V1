package com.revnext.repository.user;

import com.revnext.constants.Roles;
import com.revnext.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Roles name);
}