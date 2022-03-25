package expression;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface BigDecimalOperationDiscarder extends BigDecimalExpression {

    @Override
    default BigDecimal evaluate(BigDecimal x) {
        throw new UnsupportedOperationException("I don't do <…>");
    }
}
