package lib.simplex;

import java.util.*;

public class Tableau {
    final List<SortedMap<Integer, Double>> columns = new ArrayList<>();
    final int width;
    final int height;

    Tableau(int width, int height) {
        this.width = width;
        this.height = height;
        for (int i = 0; i < width; i++) {
            columns.add(new TreeMap<>());
        }
    }

    void emptyMinusOneColumn() {
        columns.get(width - 2).clear();
    }

    void minusOneColumn() {
        emptyMinusOneColumn();
        Map<Integer, Double> ht = columns.get(width - 2);
        for (int row = 0; row < height; row++) {
            ht.put(row, (row == 0) ? 1.0 : -1.0);
        }
    }

    Double get(int j,int i) {
        return columns.get(i).get(j);
    }

    double getOrDefault(int j,int i) {
        return columns.get(i).getOrDefault(j, 0.0);
    }

    Double getTopRow(int i) {
        return get(0, i);
    }

    double getTopRowOrDefault(int i) {
        return getOrDefault(0, i);
    }

    void set(int j,int i,double value) {
        if (Simplex.isZero(value)) {
            columns.get(i).remove(j);
        } else {
            columns.get(i).put(j, value);
        }
    }

    void add(int j,int i,double value) {
        set(j, i, getOrDefault(j, i) + value);
    }

    int findPivotColumn(boolean aux) {
        int limit = aux ? width - 1 : width - 2;
        for (int i = 0; i < limit; i++) {
            Double v = getTopRow(i);
            if (v != null && v < 0.0) {
                return i;
            }
        }
        return -1;
    }

    int findPivotRow(int i) {
        SortedMap<Integer, Double> ht = columns.get(i);
        int j = -1;
        double d = 0.0;

        for (var el : ht.entrySet()) {
            int row = el.getKey();
            if (row == 0) {
                continue;
            }

            double a = el.getValue();
            if (a <= 0.0) {
                continue;
            }

            double b = getOrDefault(row, width - 1);
            double ba = b / a;

            if (j == -1 || ba < d) {
                j = row;
                d = ba;
            }
        }

        return j;
    }

    int findSmallestB() {
        double d = 0.0;
        int j = -1;
        SortedMap<Integer, Double> ht = columns.get(width - 1);

        for (var el : ht.entrySet()) {
            if (j == -1 || el.getValue() < d) {
                j = el.getKey();
                d = el.getValue();
            }
        }

        return j;
    }

    void pivot(int j,int i) {
        // Pivot to (j,i)
        // Divide row j by the value at (j,i) ; skip column i since we set this column to the unit vector below
        double d = 1.0 / getOrDefault(j, i);
        for (int col = 0; col < width; col++) {
            if (col == i) {
                continue;
            }

            double value = getOrDefault(j, col) * d;
            set(j, col, value);

            for (var elp : columns.get(i).entrySet()) {
                if (elp.getKey() == j) {
                    continue;
                }
                add(elp.getKey(), col, -(elp.getValue() * value));
            }
        }

        // Replace column i by the unit vector with a one at column j
        columns.get(i).clear();
        columns.get(i).put(j, 1.0);
    }
}
