package com.incorparation.service.impl;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;

@Service
public class CipherService {
    public static final String AES_ENCRYPTION_STANDARD = "AES";

    @Value("${decryption.encryption.key.symmetric}")
    public String initKey;
    public Key symmetricKey = null;

    @PostConstruct
    public void init() {
        var decodedKey = Base64.decodeBase64(initKey);
        symmetricKey = new SecretKeySpec(decodedKey, 0,decodedKey.length, AES_ENCRYPTION_STANDARD);
    }

    public String encryptAES(String plainText) throws GeneralSecurityException {
        var cipher = Cipher.getInstance(AES_ENCRYPTION_STANDARD);
        cipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
        var encryptByte = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(encryptByte);
    }

    public String decryptAES(String encryptedText) throws GeneralSecurityException {
        var cipher = Cipher.getInstance(AES_ENCRYPTION_STANDARD);
        cipher.init(Cipher.DECRYPT_MODE, symmetricKey);
        var encryptedTextByte = Base64.decodeBase64(encryptedText);
        var decryptedBytes = cipher.doFinal(encryptedTextByte);

        return new String(decryptedBytes);
    }
}
