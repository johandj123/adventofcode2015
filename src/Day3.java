import lib.InputUtil;

import java.io.IOException;
import java.util.*;

public class Day3 {
    public static void main(String[] args) throws IOException {
        String input = InputUtil.readAsString("input3.txt");
        first(input);
        second(input);
    }

    private static void first(String input) {
        Position position = new Position(0, 0);
        Set<Position> positions = new HashSet<>();
        for (char c : input.toCharArray()) {
            position = updatePosition(c, position);
            positions.add(position);
        }
        System.out.println(positions.size());
    }

    private static void second(String input) {
        Position startPosition = new Position(0, 0);
        Deque<Position> deque = new ArrayDeque<>(List.of(startPosition, startPosition));
        Set<Position> positions = new HashSet<>();
        positions.add(startPosition);
        for (char c : input.toCharArray()) {
            Position position = deque.removeFirst();
            position = updatePosition(c, position);
            positions.add(position);
            deque.offerLast(position);
        }
        System.out.println(positions.size());
    }

    private static Position updatePosition(char c, Position position) {
        switch (c) {
            case '<':
                position = new Position(position.x - 1, position.y);
                break;
            case '>':
                position = new Position(position.x + 1, position.y);
                break;
            case '^':
                position = new Position(position.x, position.y - 1);
                break;
            case 'v':
                position = new Position(position.x, position.y + 1);
                break;
        }
        return position;
    }

    static class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
