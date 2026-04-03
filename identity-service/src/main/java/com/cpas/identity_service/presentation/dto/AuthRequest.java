package com.cpas.identity_service.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits including country code")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number is invalid")
    private String phoneNumber;
    @NotBlank(message = "Password is required")
    private String password;
}
