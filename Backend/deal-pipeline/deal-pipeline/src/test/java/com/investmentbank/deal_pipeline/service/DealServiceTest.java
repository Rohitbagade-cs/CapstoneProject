package com.investmentbank.deal_pipeline.service;

import com.investmentbank.deal_pipeline.dto.DealCreateRequest;
import com.investmentbank.deal_pipeline.entity.Deal;
import com.investmentbank.deal_pipeline.entity.DealStatus;
import com.investmentbank.deal_pipeline.entity.User;
import com.investmentbank.deal_pipeline.repository.DealRepository;
import com.investmentbank.deal_pipeline.repository.DealStatusRepository;
import com.investmentbank.deal_pipeline.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {

    @Mock
    private DealRepository dealRepository;
    @Mock
    private DealStatusRepository dealStatusRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DealService dealService;

    private Deal deal;
    private DealStatus status;
    private User user;

    @BeforeEach
    void setup() {
        status = new DealStatus();
        status.setId(1L);
        status.setName("OPEN");

        user = new User();
        user.setId(1L);
        user.setUsername("admin");

        deal = new Deal();
        deal.setId(1L);
        deal.setCompanyName("ABC");
        deal.setStatus(status);
        deal.setAssignedTo(user);
    }

    // ================= GET ALL DEALS =================
    @Test
    void getAllDeals_success() {
        when(dealRepository.findAll()).thenReturn(List.of(deal));
        assertEquals(1, dealService.getAllDeals().size());
    }

    // ================= CREATE DEAL =================
    @Test
    void createDeal_success() {
        DealCreateRequest req = new DealCreateRequest();
        req.setStatusId(1L);
        req.setAssignedToUserId(1L);

        when(dealStatusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dealRepository.save(any())).thenReturn(deal);

        assertNotNull(dealService.createDeal(req));
    }

    @Test
    void createDeal_statusIdNull_exception() {
        DealCreateRequest req = new DealCreateRequest();
        req.setAssignedToUserId(1L);

        assertThrows(RuntimeException.class,
                () -> dealService.createDeal(req));
    }

    @Test
    void createDeal_userIdNull_exception() {
        DealCreateRequest req = new DealCreateRequest();
        req.setStatusId(1L);

        assertThrows(RuntimeException.class,
                () -> dealService.createDeal(req));
    }

    @Test
    void createDeal_invalidStatus_exception() {
        DealCreateRequest req = new DealCreateRequest();
        req.setStatusId(1L);
        req.setAssignedToUserId(1L);

        when(dealStatusRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> dealService.createDeal(req));
    }

    @Test
    void createDeal_invalidUser_exception() {
        DealCreateRequest req = new DealCreateRequest();
        req.setStatusId(1L);
        req.setAssignedToUserId(1L);

        when(dealStatusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> dealService.createDeal(req));
    }

    // ================= UPDATE DEAL =================
    @Test
    void updateDeal_withStatusAndUser() {
        DealCreateRequest req = new DealCreateRequest();
        req.setStatusId(1L);
        req.setAssignedToUserId(1L);

        when(dealRepository.findById(1L)).thenReturn(Optional.of(deal));
        when(dealStatusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dealRepository.save(any())).thenReturn(deal);

        assertNotNull(dealService.updateDealStatus(1L, req));
    }

    @Test
    void updateDeal_dealNotFound_exception() {
        when(dealRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> dealService.updateDealStatus(1L, new DealCreateRequest()));
    }

    // ================= DELETE DEAL =================
    @Test
    void deleteDeal_success() {
        when(dealRepository.findById(1L)).thenReturn(Optional.of(deal));
        dealService.deleteDeal(1L);
        verify(dealRepository).delete(deal);
    }

    @Test
    void deleteDeal_notFound_exception() {
        when(dealRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> dealService.deleteDeal(1L));
    }

    // ================= FILTER =================
    @Test
    void getDealsByStatus_success() {
        when(dealRepository.findDealsByStatusName("OPEN"))
                .thenReturn(List.of(deal));

        assertEquals(1, dealService.getDealsByStatus("OPEN").size());
    }
}
