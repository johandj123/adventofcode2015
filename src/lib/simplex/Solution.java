package lib.simplex;

public class Solution {
    private final double value;
    private final double[] vars;

    public Solution(double value, double[] vars) {
        this.value = value;
        this.vars = vars;
    }

    public double getValue() {
        return value;
    }

    public double[] getVars() {
        return vars;
    }
}
