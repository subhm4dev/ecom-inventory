package com.ecom.inventory.repository;

import com.ecom.inventory.entity.StockAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, UUID> {
}

