import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day14 {
    public static final int TIME = 2503;

    public static void main(String[] args) throws IOException {
        List<Reindeer> reindeerList = InputUtil.readAsLines("input14.txt")
                .stream()
                .map(Reindeer::new)
                .collect(Collectors.toList());
        first(reindeerList);
        second(reindeerList);
    }

    private static void first(List<Reindeer> reindeerList) {
        int result = reindeerList.stream()
                .mapToInt(r -> r.distanceInTime(TIME))
                .max()
                .orElseThrow();
        System.out.println(result);
    }

    private static void second(List<Reindeer> reindeerList) {
        List<List<Integer>> distances = reindeerList
                .stream()
                .map(r -> r.calculateDistances(TIME))
                .map(l -> l.subList(1, TIME))
                .collect(Collectors.toList());
        List<Integer> points = reindeerList
                .stream()
                .map(r -> 0)
                .collect(Collectors.toList());
        for (int i = 0; i < distances.get(0).size(); i++) {
            int maxDistance = Integer.MIN_VALUE;
            for (int j = 0; j < points.size(); j++) {
                maxDistance = Math.max(maxDistance, distances.get(j).get(i));
            }
            for (int j = 0; j < points.size(); j++) {
                if (distances.get(j).get(i) == maxDistance) {
                    points.set(j, points.get(j) + 1);
                }
            }
        }
        int result = points.stream().mapToInt(x -> x).max().orElseThrow();
        System.out.println(result);
    }

    static class Reindeer {
        final int speed;
        final int duration;
        final int rest;

        public Reindeer(String s) {
            String[] sp = s.split("\\D+");
            speed = Integer.parseInt(sp[1]);
            duration = Integer.parseInt(sp[2]);
            rest = Integer.parseInt(sp[3]);
        }

        public int distanceInTime(int time) {
            List<Integer> distances = calculateDistances(time);
            return distances.get(time);
        }

        private List<Integer> calculateDistances(int time) {
            List<Integer> distances = new ArrayList<>();
            int distance = 0;
            while (distances.size() <= time) {
                for (int i = 0; i < duration; i++) {
                    distances.add(distance);
                    distance += speed;
                }
                for (int i = 0; i < rest; i++) {
                    distances.add(distance);
                }
            }
            return distances;
        }
    }
}
