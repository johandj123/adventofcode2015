import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day13 {
    public static void main(String[] args) throws IOException {
        Map<String, Map<String, Integer>> map = readInput();
        first(map);
        second(map);
    }

    private static void first(Map<String, Map<String, Integer>> map) {
        List<List<String>> permutations = generatePermutations(map);
        int result = permutations.stream()
                .mapToInt(permutation -> calculateHappiness(map, permutation))
                .max()
                .orElseThrow();
        System.out.println(result);
    }

    private static void second(Map<String, Map<String, Integer>> map) {
        map.put("Me", Collections.emptyMap());
        List<List<String>> permutations = generatePermutations(map);
        int result = permutations.stream()
                .mapToInt(permutation -> calculateHappiness(map, permutation))
                .max()
                .orElseThrow();
        System.out.println(result);
    }

    private static Map<String, Map<String, Integer>> readInput() throws IOException {
        List<String> input = InputUtil.readAsLines("input13.txt");
        Map<String, Map<String,Integer>> map = new HashMap<>();
        for (String s : input) {
            String[] sp = s.split("[ .]");
            int value = Integer.parseInt(s.chars().filter(c -> c >= '0' && c <= '9').collect(StringBuilder::new,
                            StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString());
            if (s.contains("lose")) {
                value = -value;
            }
            String name1 = sp[0];
            String name2 = sp[sp.length - 1];
            map.computeIfAbsent(name1, key -> new HashMap<>()).put(name2, value);
        }
        return map;
    }

    private static int calculateHappiness(Map<String, Map<String, Integer>> map, List<String> permutation) {
        int sum = 0;
        int size = permutation.size();
        for (int i = 0; i < size; i++) {
            String current = permutation.get(i);
            String next = permutation.get((i + 1) % size);
            String prev = permutation.get((i + size - 1) % size);
            sum += map.get(current).getOrDefault(next, 0);
            sum += map.get(current).getOrDefault(prev, 0);
        }
        return sum;
    }

    private static List<List<String>> generatePermutations(Map<String, Map<String,Integer>> map) {
        List<String> names = new ArrayList<>(map.keySet());
        List<String> current = new ArrayList<>();
        List<List<String>> result = new ArrayList<>();
        generatePermutations(names, current, result);
        return result;
    }

    private static void generatePermutations(List<String> names,List<String> current,List<List<String>> result) {
        if (names.isEmpty()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (String name : new ArrayList<>(names)) {
            names.remove(name);
            current.add(name);
            generatePermutations(names, current, result);
            current.remove(name);
            names.add(name);
        }
    }
}
