package com.ecom.inventory.service;

import com.ecom.inventory.model.request.BatchStockRequest;
import com.ecom.inventory.model.request.ReservationRequest;
import com.ecom.inventory.model.request.ReleaseReservationRequest;
import com.ecom.inventory.model.request.StockAdjustmentRequest;
import com.ecom.inventory.model.response.StockResponse;

import java.util.List;
import java.util.UUID;

public interface InventoryService {
    StockResponse adjustStock(UUID userId, UUID tenantId, List<String> roles, StockAdjustmentRequest request);
    StockResponse getStock(String sku, UUID locationId, UUID tenantId);
    List<StockResponse> getBatchStock(UUID tenantId, BatchStockRequest request);
    List<StockResponse> getProductLocations(String sku, UUID tenantId);
    void reserveInventory(UUID userId, UUID tenantId, ReservationRequest request);
    void releaseReservation(UUID userId, UUID tenantId, ReleaseReservationRequest request);
}

