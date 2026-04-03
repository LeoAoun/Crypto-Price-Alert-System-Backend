package com.cpas.identity_service.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cpas.identity_service.application.port.in.ManageUserUseCase;

import com.cpas.identity_service.domain.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final ManageUserUseCase manageUserUseCase;

    @GetMapping("/me")
    public User getMe(@AuthenticationPrincipal Jwt jwt) {
        String userIdStr = jwt.getClaimAsString("userId");
        UUID userId = UUID.fromString(userIdStr);
        return manageUserUseCase.getUserById(userId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticationPrincipal Jwt jwt) {
        return manageUserUseCase.getAllUsers(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        return manageUserUseCase.getUserById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {
        manageUserUseCase.deleteUser(id);
    }
}
