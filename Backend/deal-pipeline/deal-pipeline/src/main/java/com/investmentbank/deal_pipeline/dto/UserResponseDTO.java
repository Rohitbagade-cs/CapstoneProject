package com.investmentbank.deal_pipeline.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    public Long id;
    public String username;
    public String email;
    public String role;
    public boolean enabled;

    public UserResponseDTO(Long id, String username, String email, String role, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }
}

