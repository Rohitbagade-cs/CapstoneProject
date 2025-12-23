package com.investmentbank.deal_pipeline.repository;

import com.investmentbank.deal_pipeline.entity.DealStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealStatusRepository extends JpaRepository<DealStatus,Long> {

    DealStatus findByName(String name);
}
