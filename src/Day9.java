import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day9 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input9.txt");
        Graph graph = new Graph();
        for (String line : input) {
            String[] sp = line.split(" to | = ");
            graph.add(sp[0], sp[1], Integer.parseInt(sp[2]));
        }
        System.out.println(new MinVisitor(graph).distanceVisitAll());
        System.out.println(new MaxVisitor(graph).distanceVisitAll());
    }

    static class Graph {
        final Map<String, Map<String, Integer>> distances = new HashMap<>();
        final Set<String> nodes = new HashSet<>();

        public void add(String a, String b, int distance) {
            addInner(a, b, distance);
            addInner(b, a, distance);
            nodes.add(a);
            nodes.add(b);
        }

        private void addInner(String a, String b, int distance) {
            distances.computeIfAbsent(a, key -> new HashMap<>()).put(b, distance);
        }
    }

    static class MinVisitor extends Visitor {
        public MinVisitor(Graph graph) {
            super(graph);
        }

        protected int aggregate(int a,int b) {
            return Math.min(a, b);
        }

        protected int getDefaultValue() {
            return Integer.MAX_VALUE;
        }
    }

    static class MaxVisitor extends Visitor {
        public MaxVisitor(Graph graph) {
            super(graph);
        }

        protected int aggregate(int a,int b) {
            return Math.max(a, b);
        }

        protected int getDefaultValue() {
            return Integer.MIN_VALUE;
        }
    }

    static abstract class Visitor {
        final Graph graph;
        Deque<String> nodes;
        int distance;

        public Visitor(Graph graph) {
            this.graph = graph;
        }

        public int distanceVisitAll() {
            return graph.nodes.stream()
                    .mapToInt(this::distanceVisitAll)
                    .reduce(this::aggregate)
                    .orElseThrow();
        }

        private int distanceVisitAll(String start) {
            nodes = new ArrayDeque<>(List.of(start));
            distance = 0;
            return recurse(start);
        }

        private int recurse(String current) {
            int result = getDefaultValue();
            if (nodes.size() == graph.nodes.size()) {
                return distance;
            }
            for (var entry : graph.distances.get(current).entrySet()) {
                String dest = entry.getKey();
                int dist = entry.getValue();
                if (!nodes.contains(dest)) {
                    nodes.offerLast(dest);
                    distance += dist;

                    result = aggregate(result, recurse(dest));
                    distance -= dist;
                    nodes.removeLast();
                }
            }
            return result;
        }

        abstract protected int aggregate(int a,int b);

        abstract protected int getDefaultValue();
    }
}
