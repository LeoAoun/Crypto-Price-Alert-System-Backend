package com.cpas.identity_service.infrastructure.adapter.out.token;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cpas.identity_service.application.port.out.TokenGeneratorPort;
import com.cpas.identity_service.domain.model.User;
import com.cpas.identity_service.domain.model.Role;
import com.cpas.identity_service.infrastructure.component.RsaKeyManager;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements TokenGeneratorPort {

    private final RsaKeyManager rsaKeyManager;

    @org.springframework.beans.factory.annotation.Value("${app.security.jwt-issuer:identity-service}")
    private String jwtIssuer;

    @org.springframework.beans.factory.annotation.Value("${app.security.jwt-expires-in-hours:8}")
    private int jwtExpiresInHours;

    @Override
    public String generateToken(User user) {
        try {
            JWSSigner signer = new RSASSASigner(rsaKeyManager.getPrivateKey());
            long timeToLive = 3600000L * jwtExpiresInHours;

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer(jwtIssuer)
                    .claim("userId", user.getId().toString())
                    .claim("phoneNumber", user.getPhoneNumber())
                    .claim("roles", user.getRoles().stream().map(Role::name).collect(Collectors.toList()))
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + timeToLive))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).keyID("identity-service-key").build(),
                    claimsSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            log.error("Failed to generate JWT token for user {}: {}", user.getPhoneNumber(), e.getMessage(), e);
            throw new RuntimeException("Error generating JWT token", e);
        }
    }
}
