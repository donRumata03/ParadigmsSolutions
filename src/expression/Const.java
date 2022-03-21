package expression;

import expression.general.ArithmeticEngine;
import expression.general.AtomicParenthesesTrackingExpression;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public final class Const<T, Engine extends ArithmeticEngine<T>> extends AtomicParenthesesTrackingExpression<T, Engine> {
    private final T value;

    public Const(T value) {
        this.value = value;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Const aConst)) {
            return false;
        }
        return intValue == aConst.intValue
            && bigDecimalValue.equals(aConst.bigDecimalValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intValue, bigDecimalValue);
    }
}