package com.cpas.identity_service.infrastructure.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RsaKeyManager {

    @Value("${app.security.keys-path:keys}")
    private String keysPath;

    @Getter
    private PrivateKey privateKey;

    @Getter
    private PublicKey publicKey;

    @PostConstruct
    public void init() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        log.info("Loading RSA keys from path: {}", keysPath);

        File privateKeyFile = new File(keysPath, "private.pem");
        File publicKeyFile = new File(keysPath, "public.pem");

        if (!privateKeyFile.exists()) {
            throw new IllegalStateException("Private key file not found: " + privateKeyFile.getAbsolutePath());
        }
        if (!publicKeyFile.exists()) {
            throw new IllegalStateException("Public key file not found: " + publicKeyFile.getAbsolutePath());
        }

        this.privateKey = loadPrivateKey(privateKeyFile);
        this.publicKey = loadPublicKey(publicKeyFile);

        log.info("RSA keys loaded successfully. Private key algorithm: {}, Public key algorithm: {}",
                privateKey.getAlgorithm(), publicKey.getAlgorithm());
    }

    private PrivateKey loadPrivateKey(File file) throws Exception {
        try (PEMParser pemParser = new PEMParser(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (object instanceof PEMKeyPair pemKeyPair) {
                return converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
            } else if (object instanceof PrivateKeyInfo privateKeyInfo) {
                return converter.getPrivateKey(privateKeyInfo);
            } else {
                throw new IllegalStateException("Unexpected PEM object type for private key: "
                        + (object != null ? object.getClass().getName() : "null"));
            }
        }
    }

    private PublicKey loadPublicKey(File file) throws Exception {
        try (PEMParser pemParser = new PEMParser(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

            if (object instanceof SubjectPublicKeyInfo publicKeyInfo) {
                return converter.getPublicKey(publicKeyInfo);
            } else {
                throw new IllegalStateException("Unexpected PEM object type for public key: "
                        + (object != null ? object.getClass().getName() : "null"));
            }
        }
    }
}
