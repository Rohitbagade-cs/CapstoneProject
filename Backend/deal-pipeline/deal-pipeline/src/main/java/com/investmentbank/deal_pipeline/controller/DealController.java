package com.investmentbank.deal_pipeline.controller;

import com.investmentbank.deal_pipeline.dto.DealCreateRequest;
import com.investmentbank.deal_pipeline.dto.DealRequestDTO;
import com.investmentbank.deal_pipeline.dto.DealResponseDTO;
import com.investmentbank.deal_pipeline.entity.Deal;
import com.investmentbank.deal_pipeline.service.DealService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    // =========================================================
    // ✅ 1. GET ALL DEALS (ADMIN, USER)
    // =========================================================
    @GetMapping
    public List<DealResponseDTO> getAllDeals() {
        return dealService.getAllDeals();
    }

    // =========================================================
    // 🔒 2. CREATE DEAL (ADMIN ONLY)
    // =========================================================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Deal> createDeal(
            @RequestBody DealCreateRequest request) {

        Deal deal = dealService.createDeal(request);
        return ResponseEntity.ok(deal);
    }


    // =========================================================
    // 🔁 3. UPDATE DEAL STATUS (ADMIN + USER)
    // =========================================================
    @PreAuthorize("hasAnyRole('ADMIN','BANKER')")

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDealStatus(
            @PathVariable Long id,
            @RequestBody DealCreateRequest request) {

        Deal updated = dealService.updateDealStatus(id, request);
        return ResponseEntity.ok(updated);
    }


    // =========================================================
    // 🔍 4. FILTER DEALS BY STATUS (ALL AUTH USERS)
    // =========================================================
    @GetMapping("/filter")
    public List<DealResponseDTO> getDealsByStatus(
            @RequestParam String status
    ) {
        return dealService.getDealsByStatus(status);
    }
    // =========================================================
    // 🔍 5. DELETE DEALS (ALL AUTH USERS)
    // =========================================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteDeal(@PathVariable Long id) {
        dealService.deleteDeal(id);
        return ResponseEntity.ok("Deal deleted successfully");
    }

}
