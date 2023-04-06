package com.jobis.tax.domain.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class StringCryptoConverterTest {
    private StringCryptoConverter stringCryptoConverter;

    @BeforeEach
    public void setUp() {
        stringCryptoConverter = new StringCryptoConverter();
    }

    @Test
    public void testEncryptionAndDecryption() {
        String originalString = "8012268321921";

        // 암호화 테스트
        String encryptedString = stringCryptoConverter.convertToDatabaseColumn(originalString);
        assertNotEquals(originalString, encryptedString, "암호화된 문자열이 원본 문자열과 다른지 확인");

        // 복호화 테스트
        String decryptedString = stringCryptoConverter.convertToEntityAttribute(encryptedString);
        assertEquals(originalString, decryptedString, "복호화된 문자열이 원본 문자열과 동일한지 확인");
    }
}