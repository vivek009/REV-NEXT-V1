package com.revnext.repository.user;

import com.revnext.domain.user.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query("SELECT r FROM Resource r WHERE r.name = :name and r.method= :method")
    Resource findByNameAndMethod(@Param("name") String name, @Param("method") String method);
}
