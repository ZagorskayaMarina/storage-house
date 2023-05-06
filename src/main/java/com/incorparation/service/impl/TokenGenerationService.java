package com.incorparation.service.impl;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.incorparation.exception.CommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;

@Service
public class TokenGenerationService {
    @Value("${jwt.secret.key}")
    private String jwtSecretKet;

    public String encodePayload(Map<String, Object> payload) {
        var jwtSigner = new JWTSigner(jwtSecretKet);
        return jwtSigner.sign(payload);
    }

    public Map<String, Object> decodePayload(String token) {
        try {
            return new JWTVerifier(jwtSecretKet).verify(token);
        } catch (NoSuchAlgorithmException | InvalidKeyException | IOException | SignatureException | JWTVerifyException ex) {
            throw new CommonException("invalid.token");
        }
    }
}
