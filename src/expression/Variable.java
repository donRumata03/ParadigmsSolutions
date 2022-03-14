package expression;

import expression.general.AtomicParenthesesTrackingExpression;
import java.math.BigDecimal;
import java.util.Objects;

public final class Variable extends AtomicParenthesesTrackingExpression {
    private final String varName;

    public Variable(String varName) {
        this.varName = varName;
    }

    @Override
    public int evaluate(int x) {
        if (!varName.equals("x")) {
            throw new AssertionError("Variable isn't X");
        }

        return x;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        if (!varName.equals("x")) {
            throw new AssertionError("Variable isn't X");
        }

        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
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
        if (!(o instanceof Variable)) {
            return false;
        }
        Variable variable = (Variable) o;
        return varName.equals(variable.varName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName);
    }
}
