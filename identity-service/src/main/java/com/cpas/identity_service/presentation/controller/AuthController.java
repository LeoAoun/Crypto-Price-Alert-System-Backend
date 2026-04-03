package com.cpas.identity_service.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cpas.identity_service.application.port.in.AuthenticateUserUseCase;
import com.cpas.identity_service.application.port.in.RegisterUserCommand;
import com.cpas.identity_service.application.port.in.RegisterUserUseCase;
import com.cpas.identity_service.presentation.dto.AuthRequest;
import com.cpas.identity_service.presentation.dto.AuthResponse;
import com.cpas.identity_service.presentation.dto.MessageResponse;
import com.cpas.identity_service.presentation.dto.RegisterRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        registerUserUseCase.register(new RegisterUserCommand(
                request.getUsername(),
                request.getPhoneNumber(),
                request.getPassword()
        ));
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        String token = authenticateUserUseCase.authenticate(request.getPhoneNumber(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
