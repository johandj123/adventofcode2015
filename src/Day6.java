import lib.InputUtil;
import lib.RegionGrid;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Day6 {
    public static void main(String[] args) throws IOException {
        List<Instruction> instructions = InputUtil.readAsLines("input6.txt")
                .stream()
                .map(Instruction::new)
                .collect(Collectors.toList());
        RegionGrid<Content> grid = new RegionGrid<>(instructions.stream().map(Instruction::rectangle).collect(Collectors.toList()), Content::new);
        for (Instruction instruction : instructions) {
            grid.apply(instruction.rectangle(), (r, c) -> c.apply(instruction));
        }
        long first = grid.aggregate(0L, (r, c, v) -> v + (c.on ? r.area() : 0L));
        long second = grid.aggregate(0L, (r, c, v) -> v + r.area() * c.brightness);

        System.out.println(first);
        System.out.println(second);
    }

    static class Content {
        boolean on;
        int brightness;

        void apply(Instruction instruction) {
            switch (instruction.operation) {
                case ON:
                    on = true;
                    brightness++;
                    break;
                case OFF:
                    on = false;
                    brightness--;
                    if (brightness < 0) {
                        brightness = 0;
                    }
                    break;
                case TOGGLE:
                    on = !on;
                    brightness += 2;
                    break;
            }
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

        RegionGrid.Rectangle rectangle() {
            return new RegionGrid.Rectangle(x1, y1, x2 + 1, y2 + 1);
        }
    }

    enum Operation {
        ON,
        OFF,
        TOGGLE
    }
}
