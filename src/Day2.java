import lib.InputUtil;

import java.io.IOException;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {
        List<String> input = InputUtil.readAsLines("input2.txt");
        System.out.println(input.stream().mapToInt(Day2::surface).sum());
        System.out.println(input.stream().mapToInt(Day2::ribbon).sum());
    }

    private static int surface(String s) {
        String[] sp = s.split("x");
        int l = Integer.parseInt(sp[0]);
        int w = Integer.parseInt(sp[1]);
        int h = Integer.parseInt(sp[2]);
        int base = 2 * (l * w + l * h + w * h);
        int slack = l * w * h / (Math.max(Math.max(l, w), h));
        return base + slack;
    }

    private static int ribbon(String s) {
        String[] sp = s.split("x");
        int l = Integer.parseInt(sp[0]);
        int w = Integer.parseInt(sp[1]);
        int h = Integer.parseInt(sp[2]);
        int wrap = 2 * Math.min(Math.min(l + w, l + h), w + h);
        int vol = l * w * h;
        return wrap + vol;
    }
}
