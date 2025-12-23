package com.investmentbank.deal_pipeline.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DealRequestDTO {

    @NotBlank(message = "Company name cannot be empty")
    private String companyName;

    @NotNull(message = "Deal amount is required")
    @Positive(message = "Deal amount must be greater than zero")
    private Double dealAmount;

    private String description;

    @NotNull(message = "Status ID is required")
    private Long statusId;

    @NotNull(message = "Assigned User ID is required")
    private Long assignedUserId;

    public String getCompanyName() {
        return companyName;
    }

    public Double getDealAmount() {
        return dealAmount;
    }

    public String getDescription() {
        return description;
    }

    public Long getStatusId() {
        return statusId;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }
}
