package com.investmentbank.deal_pipeline.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jdk.jfr.DataAmount;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DealResponseDTO {

    private Long id;
    private String companyName;
    private Double dealAmount;
    private LocalDate createdDate;
    private String description;
    private String status;
    private String assignedTo;

    public DealResponseDTO(
            Long id,
            String companyName,
            Double dealAmount,
            LocalDate createdDate,
            String description,
            String status,
            String assignedTo){

        this.id = id;
        this.companyName = companyName;
        this.dealAmount = dealAmount;
        this.createdDate = createdDate;
        this.description = description;
        this.status = status;
        this.assignedTo = assignedTo;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public String getCompanyName() {
//        return companyName;
//    }
//
//    public Double getDealAmount() {
//        return dealAmount;
//    }
//
//    public LocalDate getCreatedDate() {
//        return createdDate;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public String getAssignedTo() {
//        return assignedTo;
//    }
}
