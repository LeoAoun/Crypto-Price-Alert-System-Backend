package com.cpas.user_preference_service.infrastructure.security;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUserDetails {
    private UUID userId;
    private String phoneNumber;
    private List<String> roles;

    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
}
