import lib.InputUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day16 {
    public static final Map<String, Integer> MFCSAM = asMap("children: 3, " +
            "cats: 7, " +
            "samoyeds: 2, " +
            "pomeranians: 3, " +
            "akitas: 0, " +
            "vizslas: 0, " +
            "goldfish: 5, " +
            "trees: 3, " +
            "cars: 2, " +
            "perfumes: 1");
    public static final Set<String> GREATER = Set.of("cats", "trees");
    public static final Set<String> FEWER = Set.of("pomeranians", "goldfish");

    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input16.txt");
        first(input);
        second(input);
    }

    private static void first(List<String> input) {
        for (String s : input) {
            String t = s.substring(s.indexOf(": ") + 2);
            if (matchFirst(asMap(t))) {
                System.out.println(s);
            }
        }
    }

    private static void second(List<String> input) {
        for (String s : input) {
            String t = s.substring(s.indexOf(": ") + 2);
            if (matchSecond(asMap(t))) {
                System.out.println(s);
            }
        }
    }

    private static Map<String, Integer> asMap(String input) {
        Map<String, Integer> map = new HashMap<>();
        String[] sp = input.split(", ");
        for (String s : sp) {
            String[] spsp = s.split(": ");
            map.put(spsp[0], Integer.parseInt(spsp[1]));
        }
        return map;
    }

    private static boolean matchFirst(Map<String, Integer> map) {
        for (var entry : MFCSAM.entrySet()) {
            String key = entry.getKey();
            if (!map.containsKey(key)) {
                continue;
            }
            int mfcsam = entry.getValue();
            int actual = map.get(key);
            if (actual != mfcsam) {
                return false;
            }
        }
        return true;
    }

    private static boolean matchSecond(Map<String, Integer> map) {
        for (var entry : MFCSAM.entrySet()) {
            String key = entry.getKey();
            if (!map.containsKey(key)) {
                continue;
            }
            int mfcsam = entry.getValue();
            int actual = map.get(key);
            if (GREATER.contains(key)) {
                if (actual <= mfcsam) {
                    return false;
                }
            } else if (FEWER.contains(key)) {
                if (actual >= mfcsam) {
                    return false;
                }
            } else if (actual != mfcsam) {
                return false;
            }
        }
        return true;
    }
}
