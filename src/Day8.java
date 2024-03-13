import lib.InputUtil;

import java.io.IOException;
import java.util.List;

public class Day8 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input8.txt");
        int first = input.stream()
                .mapToInt(s -> s.length() - countMemory(s))
                .sum();
        System.out.println(first);
        int second = input.stream()
                .mapToInt(s -> escape(s).length() - s.length())
                .sum();
        System.out.println(second);
    }

    private static int countMemory(String input) {
        String s = input.substring(1, input.length() - 1);
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            count++;
            if (s.charAt(i) == '\\') {
                i++;
                if (s.charAt(i) == 'x') {
                    i += 2;
                }
            }
        }
        return count;
    }

    private static String escape(String input) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '\\') {
                sb.append("\\\\");
            } else if (c == '\"') {
                sb.append("\\\"");
            } else {
                sb.append(c);
            }
        }
        sb.append('\"');
        return sb.toString();
    }
}
