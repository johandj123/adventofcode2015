package lib.simplex;

public class Variable {
    private final VariableType type;
    private int physVar;
    private final double goalFactor;

    Variable(VariableType type, double goalFactor) {
        this.type = type;
        this.goalFactor = goalFactor;
    }

    VariableType getType() {
        return type;
    }

    int getPhysVar() {
        return physVar;
    }

    void setPhysVar(int physVar) {
        this.physVar = physVar;
    }

    double getGoalFactor() {
        return goalFactor;
    }
}
