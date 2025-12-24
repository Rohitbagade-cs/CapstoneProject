package com.investmentbank.deal_pipeline.service;

import com.investmentbank.deal_pipeline.dto.DealCreateRequest;
import com.investmentbank.deal_pipeline.dto.DealResponseDTO;
import com.investmentbank.deal_pipeline.entity.Deal;
import com.investmentbank.deal_pipeline.entity.DealStatus;
import com.investmentbank.deal_pipeline.entity.User;
import com.investmentbank.deal_pipeline.repository.DealRepository;
import com.investmentbank.deal_pipeline.repository.DealStatusRepository;
import com.investmentbank.deal_pipeline.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DealService {

    private final DealRepository dealRepository;
    private final DealStatusRepository dealStatusRepository;
    private final UserRepository userRepository;

    public DealService(
            DealRepository dealRepository,
            DealStatusRepository dealStatusRepository,
            UserRepository userRepository) {
        this.dealRepository = dealRepository;
        this.dealStatusRepository = dealStatusRepository;
        this.userRepository = userRepository;
    }

    // ==================================================
    // GET ALL DEALS
    // ==================================================
    public List<DealResponseDTO> getAllDeals() {
        return dealRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==================================================
    // CREATE DEAL
    // ==================================================
    public Deal createDeal(DealCreateRequest request) {

        // 🛑 VALIDATION (IMPORTANT)
        if (request.getStatusId() == null) {
            throw new RuntimeException("Status ID must not be null");
        }

        if (request.getAssignedToUserId() == null) {
            throw new RuntimeException("Assigned User ID must not be null");
        }

        // 🔹 Fetch Deal Status
        DealStatus status = dealStatusRepository
                .findById(request.getStatusId())
                .orElseThrow(() -> new RuntimeException("Invalid statusId"));

        // 🔹 Fetch Assigned User
        User user = userRepository
                .findById(request.getAssignedToUserId())
                .orElseThrow(() -> new RuntimeException("Invalid assignedToUserId"));

        // 🔹 Create Deal Entity
        Deal deal = new Deal();
        deal.setCompanyName(request.getCompanyName());
        deal.setDealAmount(request.getDealAmount());
        deal.setDescription(request.getDescription());
        deal.setStatus(status);
        deal.setAssignedTo(user);
        deal.setCreatedDate(LocalDate.now());

        // 🔹 Save to DB
        return dealRepository.save(deal);
    }


    public Deal updateDealStatus(Long dealId, DealCreateRequest request) {

        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));

        // 🔹 Update basic fields
        deal.setCompanyName(request.getCompanyName());
        deal.setDealAmount(request.getDealAmount());
        deal.setDescription(request.getDescription());

        // 🔹 Update status
        if (request.getStatusId() != null) {
            DealStatus status = dealStatusRepository.findById(request.getStatusId())
                    .orElseThrow(() -> new RuntimeException("Invalid statusId"));
            deal.setStatus(status);
        }

        // 🔹 Update assigned user
        if (request.getAssignedToUserId() != null) {
            User user = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new RuntimeException("Invalid assignedToUserId"));
            deal.setAssignedTo(user);
        }

        return dealRepository.save(deal);
    }


    // ==================================================
    // DELETE DEAL
    // ==================================================
    public void deleteDeal(Long dealId) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Deal not found"));
        dealRepository.delete(deal);
    }

    // ==================================================
    // FILTER DEALS BY STATUS
    // ==================================================
    public List<DealResponseDTO> getDealsByStatus(String status) {
        return dealRepository.findDealsByStatusName(status)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // ==================================================
    // ENTITY → DTO MAPPER
    // ==================================================
    private DealResponseDTO mapToResponseDTO(Deal deal) {
        return DealResponseDTO.builder()
                .id(deal.getId())
                .companyName(deal.getCompanyName())
                .dealAmount(deal.getDealAmount())
                .description(deal.getDescription())
                .createdDate(deal.getCreatedDate())
                .status(
                        deal.getStatus() != null
                                ? deal.getStatus().getName()
                                : null
                )
                .assignedTo(
                        deal.getAssignedTo() != null
                                ? deal.getAssignedTo().getUsername()
                                : null
                )
                .build();
    }
}
