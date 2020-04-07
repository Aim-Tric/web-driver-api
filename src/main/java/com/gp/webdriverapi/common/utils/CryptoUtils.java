package com.gp.webdriverapi.common.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author Aim-Trick
 * @date 2020/4/5
 */
public class CryptoUtils {

    private static final String KEY_ALGORITHM = "AES";

    private static final String KEY = "1234567890ABCDEF";

    private static final String OFFSET = "1234567890ABCDEF";

    private static final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";

    public static String encrypt(String content, String encryptKey) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(KEY_ALGORITHM);
            keygen.init(128);
            SecretKey originalKey = new SecretKeySpec(encryptKey.getBytes(), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
            cipher.init(Cipher.ENCRYPT_MODE, originalKey);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            byte[] result = cipher.doFinal(byteContent);
            return Base64.encodeBase64String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(byte[] content, String decryptKey) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(KEY_ALGORITHM);
            keygen.init(128);
            SecretKey originalKey = new SecretKeySpec(decryptKey.getBytes(), KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
            cipher.init(Cipher.DECRYPT_MODE, originalKey);
            byte[] byteContent = Base64.decodeBase64(content);
            byte[] result = cipher.doFinal(byteContent);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String content) {
        return encrypt(content, KEY);
    }

    public static String decrypt(byte[] encryptStr) {
        return decrypt(encryptStr, KEY);
    }


}
