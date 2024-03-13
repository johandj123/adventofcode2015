import lib.InputUtil;

import java.io.IOException;

public class Day12 {
    public static void main(String[] args) throws IOException {
        String input = InputUtil.readAsString("input12.txt");
        first(input);
        second(input);
    }

    private static void first(String input) {
        String[] sp = input.split("[^\\d-]+");
        int sum = 0;
        for (String s : sp) {
            if (!s.isBlank()) {
                sum += Integer.parseInt(s);
            }
        }
        System.out.println(sum);
    }

    private static void second(String input) {
        String s = input;
        while (true) {
            int redIndex = s.indexOf("red");
            if (redIndex == -1) {
                break;
            }
            int leftIndex = scanLeft(s, redIndex);
            int rightIndex = scanRight(s, redIndex);
            if (leftIndex == -1 || rightIndex == -1) {
                s = removeRange(s, redIndex, redIndex + 3);
            } else {
                s = removeRange(s, leftIndex, rightIndex + 1);
            }
        }
        first(s);
    }

    private static int scanLeft(String s,int startIndex) {
        int index = startIndex;
        int count = 0;
        while (true) {
            char c = s.charAt(--index);
            if (c == ']' || c == '}') {
                count++;
            } else if (c == '[') {
                count--;
                if (count < 0) {
                    return -1;
                }
            } else if (c == '{') {
                count--;
                if (count < 0) {
                    return index;
                }
            }
        }
    }

    private static int scanRight(String s,int startIndex) {
        int index = startIndex;
        int count = 0;
        while (true) {
            char c = s.charAt(++index);
            if (c == '[' || c == '{') {
                count++;
            } else if (c == ']') {
                count--;
                if (count < 0) {
                    return -1;
                }
            } else if (c == '}') {
                count--;
                if (count < 0) {
                    return index;
                }
            }
        }
    }

    private static String removeRange(String s, int start, int end) {
        return s.substring(0, start) + "" + s.substring(end);
    }
}
