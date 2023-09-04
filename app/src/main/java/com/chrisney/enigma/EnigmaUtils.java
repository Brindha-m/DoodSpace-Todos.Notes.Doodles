package com.chrisney.enigma;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EnigmaUtils {
   private final static int[] data = {119, 85, 71, 84, 97, 97, 115, 79, 73, 86, 69, 108, 71, 106, 56, 113, 110, 97, 115, 82, 104, 97, 101, 115, 81, 119, 100, 115, 108, 111, 116, 52};
   public static String enigmatization(byte[] enc) {
        try {
            byte[] keyValue  = keyToBytes(data);
            byte[] result = decrypt(keyValue, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static byte[] decrypt(byte[] keyValue, byte[] encrypted)
            throws Exception {
        SecretKey skeySpec = new SecretKeySpec(keyValue, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[cipher.getBlockSize()];
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParams);
        return cipher.doFinal(encrypted);
    }
    private static byte[] keyToBytes(int[] key) {
        int size = 16 * (key.length / 16);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append((char) key[i]);
        }
        return builder.toString().getBytes();
    }
}
