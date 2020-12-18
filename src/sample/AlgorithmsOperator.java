package sample;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AlgorithmsOperator {

    public static String getHash(String inputBytes, String algorithmName) {
        String hash = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithmName);
            messageDigest.update(inputBytes.getBytes());
            byte[] digestedbytes = messageDigest.digest();
            hash = encodeHexString(digestedbytes);
            // ovo drugo je vrv bolje od prvog, ima vise opcija
            hash = new String(digestedbytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    private static String encodeHexString(byte[] byteArray) {
        StringBuilder hexStringBuffer = new StringBuilder();
        for (byte b : byteArray) {
            hexStringBuffer.append(byteToHex(b));
        }
        return hexStringBuffer.toString();
    }

    private static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public int hash(String input, boolean left, int tableSize) {
        int hash = 0;
        if (input.length() == 0) return hash - 1;
        String algorithmName = left ? "SHA-512" : "SHA-256";
        // hashCode cesto vraca isto
        hash = AlgorithmsOperator.getHash(input, algorithmName).hashCode();
        hash = Math.abs(hash) % tableSize;
        return hash;
    }
}
