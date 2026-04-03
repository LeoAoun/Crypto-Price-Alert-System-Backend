package com.cpas.user_preference_service.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.access.prepost.PreAuthorize;


import com.cpas.user_preference_service.domain.model.UserPreference;
import com.cpas.user_preference_service.presentation.dto.CreateUserPreferenceDTO;
import com.cpas.user_preference_service.application.port.in.CreateUserPreferenceCommand;
import com.cpas.user_preference_service.application.port.in.DeleteUserPreferenceCommand;
import com.cpas.user_preference_service.application.port.in.ManageUserPreferenceUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user-preferences")
public class UserPreferenceController {
    private final ManageUserPreferenceUseCase useCase;

    public UserPreferenceController(ManageUserPreferenceUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public UserPreference createUserPreference(
            @Valid @RequestBody CreateUserPreferenceDTO userPreferenceDTO,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        String phoneNumber = jwt.getClaimAsString("phoneNumber");
        return useCase.createUserPreference(new CreateUserPreferenceCommand(userPreferenceDTO, userId, phoneNumber));
    }

    @GetMapping
    public List<UserPreference> getAllUserPreferences(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        boolean isAdmin = jwt.getClaimAsStringList("roles") != null && 
                          jwt.getClaimAsStringList("roles").contains("ADMIN");
        
        if (isAdmin) {
            return useCase.getAllUserPreferences(page, size);
        }
        return useCase.getUserPreferencesByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteUserPreferenceById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaimAsString("userId"));
        List<String> roles = jwt.getClaimAsStringList("roles");
        useCase.deleteUserPreferenceById(new DeleteUserPreferenceCommand(id, userId, roles));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllUserPreferences() {
        useCase.deleteAllUserPreferences();
    }

}
