package expression.general.operations;

import expression.general.AtomicParenthesesTrackingExpression;
import java.util.Objects;

public class Variable<T> extends AtomicParenthesesTrackingExpression<T> {
    private final String varName;

    public Variable(String varName) {
        this.varName = varName;
    }

    @Override
    public T evaluate(T x) {
        if (!varName.equals("x")) {
            throw new AssertionError("Variable isn't X");
        }

        return x;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (varName) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new AssertionError("Variable name should be in { x, y, z }, it's: " + varName);
        };
    }

    @Override
    public void toStringBuilder(StringBuilder builder) {
        builder.append(varName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Variable variable)) {
            return false;
        }
        return varName.equals(variable.varName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName);
    }
}
