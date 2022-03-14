package expression;

import expression.general.AtomicParenthesesTrackingExpression;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public final class Const extends AtomicParenthesesTrackingExpression {
    private int intValue = 0;
    private Optional<BigDecimal> bigDecimalValue = Optional.empty();

    public Const(int value) {
        this.intValue = value;
    }
    public Const(BigDecimal value) {
        this.bigDecimalValue = Optional.of(value);
    }

    @Override
    public int evaluate(int x) {
        return intValue;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return intValue;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return bigDecimalValue.get();
    }

    @Override
    public void toStringBuilder(StringBuilder builder) {
        if (bigDecimalValue.isPresent()) {
            builder.append(bigDecimalValue.get());
        } else {
            builder.append(intValue);
        }
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
