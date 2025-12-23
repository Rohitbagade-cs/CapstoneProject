package com.investmentbank.deal_pipeline.repository;

import com.investmentbank.deal_pipeline.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    @Query("SELECT d FROM Deal d WHERE d.status.name = :status")
    List<Deal> findDealsByStatusName(@Param("status") String status);

}
