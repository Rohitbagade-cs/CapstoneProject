package com.investmentbank.deal_pipeline.dto;

public class DealCreateRequest {

    private String companyName;
    private Double dealAmount;
    private String description;
    private Long statusId;
    private Long assignedToUserId;

    // getters & setters

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(Double dealAmount) {
        this.dealAmount = dealAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getAssignedToUserId() {
        return assignedToUserId;
    }

    public void setAssignedToUserId(Long assignedUserId) {
        this.assignedToUserId = assignedUserId;
    }
}
