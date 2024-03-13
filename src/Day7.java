import lib.InputUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input7.txt");
        Map<String, Instruction> map = new HashMap<>();
        for (String line : input) {
            Instruction instruction = new Instruction(line);
            map.put(instruction.dest, instruction);
        }
        int a = first(map);
        second(map, a);
    }

    private static int first(Map<String, Instruction> map) {
        Evaluator evaluator = new Evaluator(map);
        int a = evaluator.eval("a");
        System.out.println(a);
        return a;
    }

    private static void second(Map<String, Instruction> map, int a) {
        Evaluator evaluator = new Evaluator(map);
        map.put("b", new Instruction(a +  " -> b"));
        System.out.println(evaluator.eval("a"));
    }

    static class Instruction {
        final String dest;
        final String src;

        Instruction(String s) {
            String[] sp = s.split(" -> ");
            src = sp[0];
            dest = sp[1];
        }
    }

    static class Evaluator {
        final Map<String, Instruction> map;
        final Map<String, Integer> cache = new HashMap<>();

        public Evaluator(Map<String, Instruction> map) {
            this.map = map;
        }

        public int eval(String s) {
            Instruction instruction = map.get(s);
            if (instruction != null) {
                if (cache.containsKey(s)) {
                    return cache.get(s);
                }
                int value = eval(instruction);
                cache.put(s, value);
                return value;
            }
            return Integer.parseInt(s);
        }

        private int eval(Instruction instruction) {
            String src = instruction.src;
            if (src.startsWith("NOT ")) {
                return (~eval(src.substring(4))) & 0xFFFF;
            }
            if (src.contains(" AND ")) {
                String[] sp = src.split(" AND ");
                return eval(sp[0]) & eval(sp[1]);
            }
            if (src.contains(" OR ")) {
                String[] sp = src.split(" OR ");
                return eval(sp[0]) | eval(sp[1]);
            }
            if (src.contains(" LSHIFT ")) {
                String[] sp = src.split(" LSHIFT ");
                return (eval(sp[0]) << eval(sp[1])) & 0xFFFF;
            }
            if (src.contains(" RSHIFT ")) {
                String[] sp = src.split(" RSHIFT ");
                return (eval(sp[0]) >> eval(sp[1])) & 0xFFFF;
            }

            return eval(src);
        }
    }
}
