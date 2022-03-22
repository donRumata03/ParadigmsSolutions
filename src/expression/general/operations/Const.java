package expression.general.operations;

import expression.general.arithmetics.ArithmeticEngine;
import expression.general.AtomicParenthesesTrackingExpression;
import java.util.Objects;

public final class Const<T> extends AtomicParenthesesTrackingExpression<T> {
    private final T value;

    public Const(T value) {
        this.value = value;
    }

    @Override
    public T evaluate(T x) {
        return value;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }

    @Override
    public void toStringBuilder(StringBuilder builder) {
        builder.append(value.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Const aConst)) {
            return false;
        }

        return value.equals(aConst.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
