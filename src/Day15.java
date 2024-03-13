import java.util.List;

public class Day15 {
    public static void main(String[] args) {
        List<List<Integer>> ingredients = List.of(
                List.of(2, 0, -2, 0, 3),
                List.of(0, 5, -3, 0, 3),
                List.of(0, 0, 5, -1, 8),
                List.of(0, -1, 0, 5, 8)
        );
        calculateScore(ingredients, false);
        calculateScore(ingredients, true);
    }

    private static void calculateScore(List<List<Integer>> ingredients, boolean require500calories) {
        int maxScore = Integer.MIN_VALUE;
        for (int x1 = 0; x1 <= 100; x1++) {
            for (int x2 = 0; x2 <= 100 - x1; x2++) {
                for (int x3 = 0; x3 < 100 - x1 - x2; x3++) {
                    int x4 = 100 - x1 - x2 - x3;
                    List<Integer> x = List.of(x1, x2, x3, x4);
                    int[] score = new int[5];
                    for (int j = 0; j < 5; j++) {
                        for (int i = 0; i < 4; i++) {
                            score[j] += x.get(i) * ingredients.get(i).get(j);
                        }
                    }
                    if (!require500calories || score[4] == 500) {
                        for (int j = 0; j < 4; j++) {
                            if (score[j] < 0) {
                                score[j] = 0;
                            }
                        }
                        int totalScore = score[0] * score[1] * score[2] * score[3];
                        maxScore = Math.max(maxScore, totalScore);
                    }
                }
            }
        }
        System.out.println(maxScore);
    }
}
