import lib.CharMatrix;
import lib.InputUtil;

import java.io.IOException;

public class Day18 {
    public static void main(String[] args) throws IOException {
        CharMatrix charMatrix = new CharMatrix(InputUtil.readAsLines("input18.txt"));
        first(charMatrix);
        second(charMatrix);
    }

    private static void first(CharMatrix charMatrix) {
        for (int i = 0; i < 100; i++) {
            charMatrix = evolve(charMatrix);
        }
        System.out.println(count(charMatrix));
    }

    private static void second(CharMatrix charMatrix) {
        for (int i = 0; i < 100; i++) {
            turnOnCorners(charMatrix);
            charMatrix = evolve(charMatrix);
        }
        turnOnCorners(charMatrix);
        System.out.println(count(charMatrix));
    }

    private static CharMatrix evolve(CharMatrix charMatrix) {
        CharMatrix newCharMatrix = new CharMatrix(charMatrix.getHeight(), charMatrix.getWidth());
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                int count = 0;
                var position = charMatrix.new Position(x, y);
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if (dx != 0 || dy != 0) {
                            var next = position.add(dx, dy);
                            if (next.getUnbounded() == '#') {
                                count++;
                            }
                        }
                    }
                }
                boolean on = false;
                if (position.get() == '#') {
                    if (count == 2 || count == 3) {
                        on = true;
                    }
                } else if (count == 3) {
                    on = true;
                }
                newCharMatrix.set(x, y, on ? '#' : '.');
            }
        }
        return newCharMatrix;
    }

    private static void turnOnCorners(CharMatrix charMatrix) {
        charMatrix.set(0, 0, '#');
        charMatrix.set(charMatrix.getWidth() - 1, 0, '#');
        charMatrix.set(0, charMatrix.getHeight() - 1, '#');
        charMatrix.set(charMatrix.getWidth() - 1, charMatrix.getHeight() - 1, '#');
    }

    private static int count(CharMatrix charMatrix) {
        int count = 0;
        for (int y = 0; y < charMatrix.getHeight(); y++) {
            for (int x = 0; x < charMatrix.getWidth(); x++) {
                if (charMatrix.get(x, y) == '#') {
                    count++;
                }
            }
        }
        return count;
    }
 }
