import lib.InputUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Day17 {
    public static void main(String[] args) throws IOException {
        List<Integer> input = InputUtil.readAsIntegers("input17.txt");
        int count = 0;
        int[] count2 = new int[input.size()];
        for (int i = 0; i < (1 << input.size()); i++) {
            int containerCount = 0;
            int sum = 0;
            for (int j = 0; j < input.size(); j++) {
                if ((i & (1 << j)) != 0) {
                    sum += input.get(j);
                    containerCount++;
                }
            }
            if (sum == 150) {
                count++;
                count2[containerCount]++;
            }
        }
        System.out.println(count);
        System.out.println(Arrays.stream(count2).filter(x -> x != 0).findFirst().orElseThrow());
    }
}
