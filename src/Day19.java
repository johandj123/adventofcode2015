import lib.InputUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day19 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input19.txt");
        String[] sp = input.get(0).split("\n");
        List<Rule> rules = Arrays.stream(sp).map(Rule::new).collect(Collectors.toList());
        String molecule = input.get(1);
        first(molecule, rules);
    }

    private static void first(String molecule, List<Rule> rules) {
        Set<String> molecules = singleReplacement(molecule, rules);
        System.out.println(molecules.size());
    }

    private static Set<String> singleReplacement(String molecule, List<Rule> rules) {
        Set<String> result = new HashSet<>();
        for (Rule rule : rules) {
            singleReplacement(molecule, rule, result);
        }
        return result;
    }

    private static void singleReplacement(String molecule, Rule rule, Set<String> result) {
        int i = -1;
        while (true) {
            i = molecule.indexOf(rule.key, i + 1);
            if (i < 0) {
                break;
            }
            String s = molecule.substring(0, i) + rule.value + molecule.substring(i + rule.key.length());
            result.add(s);
        }
    }

    static class Rule {
        final String key;
        final String value;

        public Rule(String s) {
            String[] sp = s.split(" => ");
            this.key = sp[0];
            this.value = sp[1];
        }
    }
}
