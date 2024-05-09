package lib.simplex;

public class ConstraintTerm {
    private final double factor;
    private final int var;

    public ConstraintTerm(double factor, int var) {
        this.factor = factor;
        this.var = var;
    }

    public double getFactor() {
        return factor;
    }

    public int getVar() {
        return var;
    }
}
