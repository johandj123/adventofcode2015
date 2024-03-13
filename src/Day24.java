import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day24 {
    public static void main(String[] args) throws IOException {
        List<Integer> input = InputUtil.readAsIntegers("input24.txt");
        int sum = input.stream().mapToInt(x -> x).sum();
        determineQuantumEntanglement(input, sum / 3);
        determineQuantumEntanglement(input, sum / 4);
    }

    private static void determineQuantumEntanglement(List<Integer> input, int weight) {
        List<Config> configs = new ArrayList<>();
        for (int i = 0; i < (1 << input.size()); i++) {
            int w = 0;
            for (int j = 0; j < input.size() && w <= weight; j++) {
                if ((i & (1 << j)) != 0) {
                    w += input.get(j);
                }
            }
            if (w == weight) {
                int count = 0;
                long qe = 1;
                for (int j = 0; j < input.size(); j++) {
                    if ((i & (1 << j)) != 0) {
                        count++;
                        qe *= ((long) input.get(j));
                    }
                }
                configs.add(new Config(count, qe));
            }
        }
        Config config = configs.stream().min(Comparator.comparing(Config::getCount).thenComparing(Config::getQe)).orElseThrow();
        System.out.println(config.qe);
    }

    static class Config {
        final int count;
        final long qe;

        public Config(int count, long qe) {
            this.count = count;
            this.qe = qe;
        }

        public int getCount() {
            return count;
        }

        public long getQe() {
            return qe;
        }
    }
}
