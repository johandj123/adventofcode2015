import lib.InputUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsStringGroups("input19.txt");
        String[] sp = input.get(0).split("\n");
        List<Rule> rules = Arrays.stream(sp).map(Rule::new).collect(Collectors.toList());
        String molecule = input.get(1);
        first(molecule, rules);
        second(molecule);
    }

    private static void first(String molecule, List<Rule> rules) {
        Set<String> molecules = singleReplacement(molecule, rules);
        System.out.println(molecules.size());
    }

    private static void second(String molecule) {
        Pattern pattern = Pattern.compile("[A-Z][a-z]*");
        Matcher matcher = pattern.matcher(molecule);
        int count = 0;
        int countArRn = 0;
        int countY = 0;
        while (matcher.find()) {
            String group = matcher.group();
            count++;
            if ("Ar".equals(group) || "Rn".equals(group)) {
                countArRn++;
            } else if ("Y".equals(group)) {
                countY++;
            }
        }
        int result = count - countArRn - 2 * countY - 1;
        System.out.println(result);
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
