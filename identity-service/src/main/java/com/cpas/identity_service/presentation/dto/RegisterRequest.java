package com.cpas.identity_service.presentation.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits including country code")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number is invalid")
    private String phoneNumber;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
