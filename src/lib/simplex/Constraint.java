package lib.simplex;

public class Constraint {
    private final ConstraintType type;
    private final int firstTerm;
    private int countTerms;
    private final double value;
    private int physConstraint;

    public Constraint(ConstraintType type, double value, int firstTerm) {
        this.type = type;
        this.firstTerm = firstTerm;
        this.value = value;
    }

    ConstraintType getType() {
        return type;
    }

    int getFirstTerm() {
        return firstTerm;
    }

    int getCountTerms() {
        return countTerms;
    }

    void incCountTerms() {
        countTerms++;
    }

    double getValue() {
        return value;
    }

    int getPhysConstraint() {
        return physConstraint;
    }

    void setPhysConstraint(int physConstraint) {
        this.physConstraint = physConstraint;
    }
}
