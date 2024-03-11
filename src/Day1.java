import lib.InputUtil;

import java.io.IOException;

public class Day1 {
    public static void main(String[] args) throws IOException {
        String input = InputUtil.readAsString("input1.txt");
        first(input);
        second(input);
    }

    private static void first(String input) {
        long count = input.chars().filter(c -> c == '(').count() - input.chars().filter(c -> c == ')').count();
        System.out.println(count);
    }

    private static void second(String input) {
        long floor = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '(') {
                floor++;
            } else if (c == ')') {
                floor--;
                if (floor < 0) {
                    System.out.println(i + 1);
                    return;
                }
            }
        }
    }
}
