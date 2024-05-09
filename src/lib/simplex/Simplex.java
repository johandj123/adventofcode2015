package lib.simplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simplex {
    private static final double EPSILON = 0.0000001;

    private final List<Variable> variables = new ArrayList<>();
    private final List<Constraint> constraints = new ArrayList<>();
    private final List<ConstraintTerm> terms = new ArrayList<>();

    public int addVariable(VariableType type, double goalFactor) {
        int c = variables.size();
        variables.add(new Variable(type, goalFactor));
        if (type == VariableType.BOOLEAN) {
            addConstraint(ConstraintType.LESS_OR_EQUAL, 1.0);
            // TODO Add constraint
        }
        return c;
    }

    public void addConstraint(ConstraintType type, double value) {
        constraints.add(new Constraint(type, value, terms.size()));
    }

    public void addConstraintTerm(double factor, int var) {
        terms.add(new ConstraintTerm(factor, var));
        constraints.get(constraints.size() - 1).incCountTerms();
    }

    private Solution solveInner() {
        int countPhysVars = 0;
        for (Variable variable : variables) {
            variable.setPhysVar(countPhysVars);
            if (variable.getType().isNonNegative()) {
                countPhysVars++;
            } else {
                countPhysVars += 2;
            }
        }
        int countPhysConstraints = 0;
        for (Constraint constraint : constraints) {
            constraint.setPhysConstraint(countPhysConstraints);
            if (constraint.getType() == ConstraintType.EQUAL) {
                countPhysConstraints += 2;
            } else {
                countPhysConstraints++;
            }
        }

        int width = countPhysVars + countPhysConstraints + 2;
        int height = countPhysConstraints + 1;
        Tableau t = new Tableau(width, height);

        // Fill in b
        boolean aux = false;
        int rowIndex = 1;
        for (Constraint constraint : constraints) {
            if (constraint.getValue() < 0.0 || (constraint.getType() == ConstraintType.EQUAL && !isZero(constraint.getValue()))) {
                aux = true;
            }
            t.add(rowIndex++, width - 1, constraint.getValue());
            if (constraint.getType() == ConstraintType.EQUAL) {
                t.add(rowIndex++, width - 1, -constraint.getValue());
            }
        }

        // Fill in A
        rowIndex = 1;
        for (Constraint constraint : constraints) {
            for (int k = constraint.getFirstTerm(); k < constraint.getFirstTerm() + constraint.getCountTerms(); k++) {
                ConstraintTerm term = terms.get(k);
                Variable v = variables.get(term.getVar());
                t.add(rowIndex, v.getPhysVar(), term.getFactor());
                if (!v.getType().isNonNegative()) {
                    t.add(rowIndex, v.getPhysVar() + 1, -term.getFactor());
                }
            }
            rowIndex++;

            if (constraint.getType() == ConstraintType.EQUAL) {
                for (int k = constraint.getFirstTerm(); k < constraint.getFirstTerm() + constraint.getCountTerms(); k++) {
                    ConstraintTerm term = terms.get(k);
                    Variable v = variables.get(term.getVar());
                    t.add(rowIndex, v.getPhysVar(), -term.getFactor());
                    if (!v.getType().isNonNegative()) {
                        t.add(rowIndex, v.getPhysVar() + 1, term.getFactor());
                    }
                }
                rowIndex++;
            }
        }
        for (int i = 0; i < countPhysConstraints; i++) {
            t.add(i + 1, i + countPhysVars, 1.0);
        }

        // Determine initial base
        int[] baseColumns = new int[width];
        Arrays.fill(baseColumns, -1);
        for (int i = 0; i < countPhysConstraints; i++) {
            baseColumns[countPhysVars + i] = i;
        }
        int[] baseVectors = new int[countPhysConstraints];
        for (int i = 0; i < countPhysConstraints; i++) {
            baseVectors[i] = countPhysVars + i;
        }

        // Solve auxiliary problem, if needed
        if (aux) {
            t.minusOneColumn();

            // Solve aux tableau
            boolean first = true;
            while (true) {
                int i;
                int j;
                if (first) {
                    first = false;

                    // Column is the -1 column
                    i = width - 2;
                    j = t.findSmallestB();
                } else {
                    // Determine column to work on
                    i = t.findPivotColumn(true);
                    if (i == -1) {
                        break;
                    }

                    // Determine row to work on
                    j = t.findPivotRow(i);
                }

                // If now row coule be found, there is an unbounded direction
                if (j == -1) {
                    return null;
                }

                // Pivot
                t.pivot(j, i);

                // Update base
                baseColumns[baseVectors[j - 1]] = -1;
                baseVectors[j - 1] = i;
                baseColumns[i] = j - 1;
            }

            if (t.getTopRow(width - 1) != null) {
                // No feasible base
                return null;
            }

            // Make sure that the -1 column is no longer in the base
            if (baseColumns[width - 2] != -1) {
                int j = baseColumns[width - 2] + 1;
                int i;
                for (i = 0; i < width - 2; i++) {
                    if (baseColumns[i] != -1) {
                        continue;
                    }
                    if (t.get(j, i) != null) {
                        break;
                    }
                }
                if (i == width - 2) {
                    // No feasible base
                    return null;
                }

                // Pivot
                t.pivot(j, i);

                // Update Base
                baseColumns[baseVectors[j - 1]] = -1;
                baseVectors[j - 1] = i;
                baseColumns[i] = j - 1;
            }
        }

        // Fill in -c
        rowIndex = 0;
        for (Variable variable : variables) {
            t.set(0, rowIndex++, -variable.getGoalFactor());
            if (!variable.getType().isNonNegative()) {
                t.set(0, rowIndex++, variable.getGoalFactor());
            }
        }
        while (rowIndex < width) {
            t.set(0, rowIndex++, 0.0);
        }

        if (aux) {
            for (int i = 0; i < width - 2; i++) {
                if (baseColumns[i] != -1) {
                    Double d = t.getTopRow(i);
                    if (d != null) {
                        for (int k = 0; k < width; k++) {
                            Double e = t.get(baseColumns[i] + 1, k);
                            if (e != null) {
                                t.add(0, k, -(d * e));
                            }
                        }
                    }
                }
            }

            t.emptyMinusOneColumn();
        }

        // Solve
        while (true) {
            // Determine column to work on
            int i = t.findPivotColumn(false);
            if (i == -1) {
                break;
            }

            // Determine row to work on
            int j = t.findPivotRow(i);

            // If now row could be found, there is an unbounded direction
            if (j == -1) {
                return null;
            }

            // Change this row into a unit vector
            t.pivot(j, i);

            // Update Base
            baseColumns[baseVectors[j - 1]] = -1;
            baseVectors[j - 1] = i;
            baseColumns[i] = j - 1;
        }

        // Determine solution
        double[] physsol = new double[countPhysVars + countPhysConstraints];
        for (int i = 0; i < countPhysConstraints; i++) {
            double v = t.getOrDefault(i + 1, width - 1);
            if (v < 0.0) {
                // Not feasible, row base_vectors[i], value v
                return null;
            }
            physsol[baseVectors[i]] = v;
        }
        double[] sol = new double[variables.size()];
        rowIndex = 0;
        for (int i = 0; i < variables.size(); i++) {
            Variable variable = variables.get(i);
            if (variable.getType().isNonNegative()) {
                sol[i] = physsol[rowIndex++];
            } else {
                sol[i] = physsol[rowIndex] - physsol[rowIndex + 1];
                rowIndex += 2;
            }
            if (variable.getType().isInteger()) {
                sol[i] = Math.floor(sol[i] + 0.5);
            }
        }
        double value = t.getTopRowOrDefault(width - 1);
        return new Solution(value, sol);
    }

    public Solution solve() {
        return solveIntegerBranchAndBound(null);
    }

    private Solution solveIntegerBranchAndBound(Solution solution) {
        // Determine the non-integral solution
        Solution nonIntegralSolution = solveInner();

        // Return if there is no solution or it has a lower value than the one we already have
        if (nonIntegralSolution == null ||
                (solution != null && nonIntegralSolution.getValue() <= solution.getValue())) {
            return solution;
        }

        int i = 0;
        while (i < variables.size()) {
            if (variables.get(i).getType().isInteger() && !isIntegral(nonIntegralSolution.getVars()[i])) {
                break;
            }
            i++;
        }

        if (i == variables.size()) {
            return nonIntegralSolution;
        }

        // Branch and bound
        addConstraint(ConstraintType.LESS_OR_EQUAL, Math.floor(nonIntegralSolution.getVars()[i]) + 2 * EPSILON);
        addConstraintTerm(1.0, i);
        solution = solveIntegerBranchAndBound(solution);
        constraints.remove(constraints.size() - 1);
        terms.remove(terms.size() - 1);

        addConstraint(ConstraintType.LESS_OR_EQUAL, -Math.ceil(nonIntegralSolution.getVars()[i]) - 2 * EPSILON);
        addConstraintTerm(-1.0, i);
        solution = solveIntegerBranchAndBound(solution);
        constraints.remove(constraints.size() - 1);
        terms.remove(terms.size() - 1);

        return solution;
    }

    public static boolean isZero(double v) {
        return (v > -EPSILON && v < EPSILON);
    }

    public static boolean isIntegral(double v) {
        return isZero(v - Math.floor(v + 0.5));
    }
}
