package com.ecom.inventory.repository;

import com.ecom.inventory.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    List<Location> findByTenantIdAndActiveTrue(UUID tenantId);
    java.util.Optional<Location> findByIdAndTenantId(UUID id, UUID tenantId);
}

