public class Day25 {
    public static void main(String[] args) {
        System.out.println(valueAt(2947L, 3029L));
    }

    private static long valueAt(long row,long column) {
        long index = calculateIndex(row, column);
        long factor = calculateFactor(index - 1L);
        return (20151125L * factor) % 33554393L;
    }

    private static long calculateIndex(long row, long column) {
        long result = 1L;
        long step = 1L;
        for (int i = 1; i < row; i++) {
            result += step;
            step++;
        }
        step++;
        for (int i = 1; i < column; i++) {
            result += step;
            step++;
        }
        return result;
    }

    private static long calculateFactor(long n) {
        if (n == 0) {
            return 1L;
        } else if ((n % 2L) == 0) {
            long f = calculateFactor(n / 2L);
            return (f * f) % 33554393L;
        } else {
            long f = calculateFactor(n - 1L);
            return (252533L * f) % 33554393L;
        }
    }
}
