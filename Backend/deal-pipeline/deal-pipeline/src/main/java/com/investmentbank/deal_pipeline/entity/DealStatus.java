package com.investmentbank.deal_pipeline.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "deal_status")
public class DealStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // ✅ MUST BE PRESENT
    public String getName() {
        return name;
    }

    // optional but recommended
    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //
//    private String statusName;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getStatusName() {
//        return statusName;
//    }
//
//    public void setStatusName(String statusName) {
//        this.statusName = statusName;
//    }
}
