package avia.cloud.client.util;

import java.security.SecureRandom;

public class SecretKeyGenerator {
    private static final int KEY_LENGTH_BYTES = 32; // 256 bits

    public static String generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[KEY_LENGTH_BYTES];
        secureRandom.nextBytes(keyBytes);
        StringBuilder hexString = new StringBuilder();
        for (byte keyByte : keyBytes) {
            String hex = Integer.toHexString(0xff & keyByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}

