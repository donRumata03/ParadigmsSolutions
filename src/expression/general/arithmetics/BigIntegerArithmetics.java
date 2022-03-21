package expression.general.arithmetics;

import expression.general.exceptions.ExpressionArithmeticException;
import java.math.BigInteger;

public class BigIntegerArithmetics implements ArithmeticEngine<BigInteger> {

    @Override
    public BigInteger fromInt(int value) {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger add(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return left.add(right);
    }

    // …
}
