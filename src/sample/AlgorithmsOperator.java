package sample;

import javafx.scene.control.ComboBox;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AlgorithmsOperator {

    private final ComboBox<String> dropListLeft;
    private final ComboBox<String> dropListRight;
    private final String[] algorithmNames = {"MD2", "MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512"};
    private String leftAlgorithm;
    private String rightAlgorithm;

    public AlgorithmsOperator(ComboBox<String> dropListLeft, ComboBox<String> dropListRight) {
        this.dropListLeft = dropListLeft;
        this.dropListRight = dropListRight;

        dropListLeft.getItems().addAll(algorithmNames);
        dropListLeft.setValue("MD2");
        leftAlgorithm = "MD2";

        dropListRight.getItems().addAll(algorithmNames);
        dropListRight.setValue("MD2");
        rightAlgorithm = "MD2";

        dropListLeft.valueProperty().addListener((ov, oldString, newString) -> leftAlgorithm = newString);
        dropListRight.valueProperty().addListener((ov, oldString, newString) -> rightAlgorithm = newString);

    }

    public static String getHash(String inputBytes, String algorithmName) {
        String transformed = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithmName);
            messageDigest.update(inputBytes.getBytes());
            byte[] digestedbytes = messageDigest.digest();
            transformed = encodeHexString(digestedbytes);
            transformed = new String(digestedbytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return transformed;
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
        String algorithmName = left ? leftAlgorithm : rightAlgorithm;
        // hashCode cesto vraca isto
        hash = AlgorithmsOperator.getHash(input, algorithmName).hashCode();
        hash = Math.abs(hash) % tableSize;
        return hash;
    }

    public boolean areSame() {
        return leftAlgorithm.equals(rightAlgorithm);
    }

    public String getLeftAlgorithm() {
        return leftAlgorithm;
    }

    public String getRightAlgorithm() {
        return rightAlgorithm;
    }
}
