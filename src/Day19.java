import lib.BigRational;
import lib.InputUtil;
import lib.simplex.ConstraintType;
import lib.simplex.Simplex;
import lib.simplex.Solution;
import lib.simplex.VariableType;

import java.io.IOException;
import java.util.*;
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
        second(molecule, rules);
    }

    private static List<String> listAtoms(String molecule) {
        Pattern pattern = Pattern.compile("([A-Z][a-z]*)|e");
        Matcher matcher = pattern.matcher(molecule);
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    private static void first(String molecule, List<Rule> rules) {
        Set<String> molecules = singleReplacement(molecule, rules);
        System.out.println(molecules.size());
    }

    private static void second(String molecule, List<Rule> rules) {
        List<String> startAtoms = List.of("e");
        List<String> moleculeAtoms = listAtoms(molecule);
        Set<String> allAtoms = new HashSet<>(moleculeAtoms);
        for (Rule rule : rules) {
            allAtoms.addAll(listAtoms(rule.key));
            allAtoms.addAll(listAtoms(rule.value));
        }
        Simplex simplex = new Simplex();
        for (int i = 0; i < rules.size(); i++) {
            simplex.addVariable(VariableType.INTEGER_NONNEGATIVE, BigRational.ONE);
        }
        for (String atom : allAtoms) {
            long left = startAtoms.stream().filter(atom::equals).count();
            long right = moleculeAtoms.stream().filter(atom::equals).count();
            long balance = right - left;
            simplex.addConstraint(ConstraintType.EQUAL, new BigRational(balance));
            for (int i = 0; i < rules.size(); i++) {
                Rule rule = rules.get(i);
                long leftr = listAtoms(rule.key).stream().filter(atom::equals).count();
                long rightr = listAtoms(rule.value).stream().filter(atom::equals).count();
                long balancer = rightr - leftr;
                if (balance != 0L) {
                    simplex.addConstraintTerm(new BigRational(balancer), i);
                }
            }
        }
        Solution solution = simplex.solve();
        if (solution != null) {
            System.out.println(solution.getValue());
        }
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
