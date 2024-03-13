import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day6 {
    public static void main(String[] args) throws IOException {
        List<Instruction> instructions = InputUtil.readAsLines("input6.txt")
                .stream()
                .map(Instruction::new)
                .collect(Collectors.toList());
        Grid grid = new Grid(instructions);
        System.out.println(grid.count());
        System.out.println(grid.totalBrightness());
    }

    static class Grid {
        List<Integer> h;
        List<Integer> v;
        boolean[][] grid;
        int[][] brightness;

        public Grid(List<Instruction> instructions) {
            SortedSet<Integer> x = new TreeSet<>();
            SortedSet<Integer> y = new TreeSet<>();
            for (Instruction instruction : instructions) {
                x.add(instruction.x1);
                y.add(instruction.y1);
                x.add(instruction.x2 + 1);
                y.add(instruction.y2 + 1);
            }
            h = new ArrayList<>(x);
            v = new ArrayList<>(y);
            grid = new boolean[v.size() - 1][h.size() - 1];
            brightness = new int[v.size() - 1][h.size() - 1];
            instructions.forEach(this::toggle);
        }

        private void toggle(Instruction instruction) {
            int x1 = h.indexOf(instruction.x1);
            int y1 = v.indexOf(instruction.y1);
            int x2 = h.indexOf(instruction.x2 + 1);
            int y2 = v.indexOf(instruction.y2 + 1);
            for (int y = y1; y < y2; y++) {
                for (int x = x1; x < x2; x++) {
                    switch (instruction.operation) {
                        case ON:
                            grid[y][x] = true;
                            brightness[y][x]++;
                            break;
                        case OFF:
                            grid[y][x] = false;
                            brightness[y][x]--;
                            if (brightness[y][x] < 0) {
                                brightness[y][x] = 0;
                            }
                            break;
                        case TOGGLE:
                            grid[y][x] = !grid[y][x];
                            brightness[y][x] += 2;
                            break;
                    }
                }
            }
        }

        public int count() {
            int result = 0;
            for (int y = 0; y < v.size() - 1; y++) {
                int yy = v.get(y + 1) - v.get(y);
                for (int x = 0; x < h.size() - 1; x++) {
                    if (grid[y][x]) {
                        int xx = h.get(x + 1) - h.get(x);
                        int area = xx * yy;
                        result += area;
                    }
                }
            }
            return result;
        }

        public long totalBrightness() {
            long result = 0L;
            for (int y = 0; y < v.size() - 1; y++) {
                int yy = v.get(y + 1) - v.get(y);
                for (int x = 0; x < h.size() - 1; x++) {
                    int xx = h.get(x + 1) - h.get(x);
                    long area = ((long) xx) * ((long) yy);
                    long value = brightness[y][x];
                    result += (area * value);
                }
            }
            return result;
        }
    }

    static class Instruction {
        final int x1;
        final int y1;
        final int x2;
        final int y2;
        final Operation operation;

        Instruction(String s) {
            String[] sp = s.split("\\D+");
            x1 = Integer.parseInt(sp[1]);
            y1 = Integer.parseInt(sp[2]);
            x2 = Integer.parseInt(sp[3]);
            y2 = Integer.parseInt(sp[4]);
            if (s.contains("on")) {
                operation = Operation.ON;
            } else if (s.contains("off")) {
                operation = Operation.OFF;
            } else if (s.contains("toggle")) {
                operation = Operation.TOGGLE;
            } else {
                throw new IllegalArgumentException("Unknown operation");
            }
        }
    }

    enum Operation {
        ON,
        OFF,
        TOGGLE
    }
}
