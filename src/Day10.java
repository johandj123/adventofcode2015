import lib.InputUtil;

import java.io.IOException;

public class Day10 {
    public static void main(String[] args) throws IOException {
        String s = InputUtil.readAsString("input10.txt");
        for (int i = 0; i < 50; i++) {
            s = next(s);
        }
        System.out.println(s.length());
    }

    private static String next(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ) {
            char c = s.charAt(i);
            int ii = i;
            while (i < s.length() && s.charAt(i) == c) {
                i++;
            }
            sb.append(i - ii);
            sb.append(c);
        }
        return sb.toString();
    }
}
