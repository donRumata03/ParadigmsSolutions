package expression.general;

import java.math.BigInteger;

public class BigIntegerArithmetics implements ArithmeticEngine<BigInteger> {

    @Override
    public BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }
}
