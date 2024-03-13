import lib.InputUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Day5 {
    public static final Set<Character> VOWELS = Set.of('a', 'e', 'i', 'o', 'u');
    public static final List<String> FORBIDDEN = List.of("ab", "cd", "pq", "xy");

    public static void main(String[] args) throws IOException {
        List<String> strings = InputUtil.readAsLines("input5.txt");
        System.out.println(strings.stream().filter(Day5::first).count());
        System.out.println(strings.stream().filter(Day5::second).count());
    }

    private static boolean first(String s) {
        long vowelCount = s.chars().filter(o -> VOWELS.contains((char) o)).count();
        if (vowelCount < 3) return false;
        if (!consecutive(s)) return false;
        return FORBIDDEN.stream().noneMatch(s::contains);
    }

    private static boolean second(String s) {
        return repeatPair(s) && repeatWithOneLetterInBetween(s);
    }

    private static boolean consecutive(String s) {
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                return true;
            }
        }
        return false;
    }

    private static boolean repeatPair(String s) {
        for (int i = 0; i < s.length() - 1; i++) {
            String pair = s.substring(i, i + 2);
            for (int j = 0; j < s.length() - 1; j++) {
                if (i - 1 <= j && j <= i + 1) {
                    continue;
                }
                String pair2 = s.substring(j, j + 2);
                if (pair.equals(pair2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean repeatWithOneLetterInBetween(String s) {
        for (int i = 0; i < s.length() - 2; i++) {
            if (s.charAt(i) == s.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
}
