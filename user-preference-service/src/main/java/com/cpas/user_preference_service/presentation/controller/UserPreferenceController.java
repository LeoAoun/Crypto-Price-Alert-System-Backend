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
import com.cpas.user_preference_service.infrastructure.security.AuthenticatedUser;
import com.cpas.user_preference_service.infrastructure.security.AuthenticatedUserDetails;
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
            @AuthenticatedUser AuthenticatedUserDetails user) {
        return useCase.createUserPreference(new CreateUserPreferenceCommand(
            userPreferenceDTO, 
            user.getUserId(), 
            user.getPhoneNumber()
        ));
    }

    @GetMapping
    public List<UserPreference> getAllUserPreferences(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticatedUser AuthenticatedUserDetails user) {
        
        if (user.hasRole("ADMIN")) {
            return useCase.getAllUserPreferences(page, size);
        }
        return useCase.getUserPreferencesByUserId(user.getUserId());
    }

    @DeleteMapping("/{id}")
    public void deleteUserPreferenceById(
            @PathVariable UUID id,
            @AuthenticatedUser AuthenticatedUserDetails user) {
        useCase.deleteUserPreferenceById(new DeleteUserPreferenceCommand(id, user.getUserId(), user.getRoles()));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllUserPreferences() {
        useCase.deleteAllUserPreferences();
    }

}
