package com.incorparation.util;

import com.incorparation.exception.CommonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class EncryptDecrypt {
    private static final Logger LOGGER = LogManager.getLogger(EncryptDecrypt.class);
    private static final byte[] KEY_VALUES = new byte[]{'T', 'k', 'N', 'x', 'y', 'g', 'V', 'a', 'u', 'i', 'M', 'b', 'c', 'j', 'E', 'l'};
    private static final String ALGORITHM_VALUE = "AES";

    private EncryptDecrypt() {
    }

    public static byte[] getSalt() {
        try {
            var random = SecureRandom.getInstance("SHA1PRNG");
            var salt = new byte[8];
            random.nextBytes(salt);

            return salt;
        } catch (Exception ex) {
            LOGGER.error("Error while generate salt data", ex);
            throw new CommonException("Generating salt error");
        }
    }

    public static String byteToBase64(byte[] data) {
        return new String(Base64.encodeBase64(data));
    }

    public static byte[] stringToByteBase64(String data) {
        try {
            return Base64.decodeBase64(data);
        } catch (Exception ex) {
            LOGGER.error("Error to convert String to Byte", ex);
            throw new CommonException("Error to convert String to Byte");
        }
    }

    public static String encrypt(String data) {
        try {
            var key = generateKey();
            var cipher = Cipher.getInstance(ALGORITHM_VALUE);

            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(data.getBytes());

            return byteToBase64(encryptedData);
        } catch (Exception ex) {
            LOGGER.error("Error to encrypt data", ex);
            throw new CommonException("Error to encrypt data", ex);
        }
    }

    public static byte[] getHash(String password, byte[] salt) {
        try {
            var digest = MessageDigest.getInstance("SHA-1");

            digest.reset();
            digest.update(salt);

            var input = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            for (int i = 1; i < 1000; i++) {
                digest.reset();
                input = digest.digest(input);
            }

            return input;
        } catch (Exception ex) {
            LOGGER.error("Error to get hash", ex);
            throw new CommonException("Error to get hash");
        }
    }

    private static Key generateKey() {
        return new SecretKeySpec(KEY_VALUES, ALGORITHM_VALUE);
    }
}
