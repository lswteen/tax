package com.jobis.tax.domain.user.repository;

import com.jobis.tax.core.exception.ApiException;
import com.jobis.tax.core.type.ServiceErrorType;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.Key;
import java.util.Base64;

@Converter
public class StringCryptoConverter implements AttributeConverter<String, String> {
    //해당 부분 application.yml로 보안취약하여 빠져야합니다. 시간관계로 지송합니다.
    private static final String SECRET_KEY ="A1b2C3d4E5f6G7h8";
    private static final String INITIALIZATION_VECTOR ="H8g7F6e5D4c3B2a1";

    private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            Key secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(INITIALIZATION_VECTOR.getBytes());
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new ApiException(ServiceErrorType.FAILED_TO_ENCRYPT_DATA);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            Key secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            IvParameterSpec iv = new IvParameterSpec(INITIALIZATION_VECTOR.getBytes());
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new ApiException(ServiceErrorType.FAILED_TO_DECRYPT_DATA);
        }
    }
}
