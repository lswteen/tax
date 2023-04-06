package com.jobis.tax.application.security.util;

import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class ResidentNumberEncryptor {
    private static final String PASSWORD = "your-password"; //
    private static final String SALT = "your-salt"; //

    private BytesEncryptor encryptor;

    public ResidentNumberEncryptor() {
        encryptor = Encryptors.stronger(PASSWORD, SALT);
    }

    public String encrypt(String residentNumber) {
        byte[] encryptedBytes = encryptor.encrypt(residentNumber.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedResidentNumber) {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedResidentNumber);
        byte[] decryptedBytes = encryptor.decrypt(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
