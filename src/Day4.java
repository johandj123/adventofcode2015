import lib.InputUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Day4 {
    public static void main(String[] args) throws IOException {
        String input = InputUtil.readAsString("input4.txt");
        first(input);
        second(input);
    }

    private static void first(String input) {
        for (int i = 1; ; i++) {
            String hash = md5(input + i);
            if (hash.startsWith("00000")) {
                System.out.println(i);
                return;
            }
        }
    }

    private static void second(String input) {
        for (int i = 1; ; i++) {
            String hash = md5(input + i);
            if (hash.startsWith("000000")) {
                System.out.println(i);
                return;
            }
        }
    }

    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
