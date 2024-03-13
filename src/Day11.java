import lib.InputUtil;

import java.io.IOException;

public class Day11 {
    public static void main(String[] args) throws IOException {
        String input = InputUtil.readAsString("input11.txt");
        char[] chars = input.toCharArray();
        findNext(chars);
        findNext(chars);
    }

    private static void findNext(char[] chars) {
        do {
            next(chars);
        } while (!valid(chars));
        System.out.println(asString(chars));
    }

    private static String asString(char[] chars) {
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }

    private static void next(char[] chars) {
        int i = chars.length - 1;
        while (i > 0) {
            chars[i]++;
            if (chars[i] > 'z') {
                chars[i] = 'a';
                i--;
            } else {
                break;
            }
        }
    }

    private static boolean valid(char[] chars) {
        return straight(chars) && legal(chars) && pair(chars);
    }

    private static boolean straight(char[] chars) {
        for (int i = 0; i < chars.length - 2; i++) {
            if (chars[i] == chars[i + 1] - 1 && chars[i + 1] == chars[i + 2] - 1) {
                return true;
            }
        }
        return false;
    }

    private static boolean legal(char[] chars) {
        for (char c : chars) {
            if (c == 'i' || c == 'o' || c == 'l') {
                return false;
            }
        }
        return true;
    }

    private static boolean pair(char[] chars) {
        for (int i = 0; i < chars.length - 1; i++) {
            for (int j = 0; j < chars.length - 1; j++) {
                if (i - 1 <= j && j <= i + 1) {
                    continue;
                }
                if (chars[i] == chars[i + 1] && chars[j] == chars[j + 1]) {
                    return true;
                }
            }
        }
        return false;
    }
}
