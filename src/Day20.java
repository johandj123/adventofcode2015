import lib.InputUtil;

import java.io.IOException;

public class Day20 {
    public static void main(String[] args) throws IOException {
        int value = InputUtil.readAsIntegers("input20.txt").get(0);
        first(value);
        second(value);
    }

    private static void first(int value) {
        int[] presents = new int[value / 10 + 1];
        for (int i = 1; i < presents.length; i++) {
            for (int j = i; j < presents.length; j += i) {
                presents[j] += (10 * i);
            }
        }
        for (int i = 1; i < presents.length; i++) {
            if (presents[i] >= value) {
                System.out.println(i);
                return;
            }
        }
    }

    private static void second(int value) {
        int[] presents = new int[value / 10 + 1];
        for (int i = 1; i < presents.length; i++) {
            for (int j = i; j < presents.length && j <= 50 * i; j += i) {
                presents[j] += (11 * i);
            }
        }
        for (int i = 1; i < presents.length; i++) {
            if (presents[i] >= value) {
                System.out.println(i);
                return;
            }
        }
    }
}
