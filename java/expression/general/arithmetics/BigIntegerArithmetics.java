package expression.general.arithmetics;

import expression.general.exceptions.ExpressionArithmeticException;
import java.math.BigInteger;

public class BigIntegerArithmetics implements ArithmeticEngine<BigInteger> {

    @Override
    public BigInteger fromInt(int value) {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger parseSignedInt(String toParse) {
        return new BigInteger(toParse);
    }

    @Override
    public BigInteger abs(BigInteger argument) {
        return argument.abs();
    }

    @Override
    public BigInteger negate(BigInteger argument) {
        return argument.negate();
    }

    @Override
    public BigInteger pow(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return ExpressionArithmeticException.wrapComputationWithException(() ->  left.pow(right.intValue()));
    }

    @Override
    public BigInteger log(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        throw new ExpressionArithmeticException("«log» isn't supported by BigIntegers");
    }

    @Override
    public BigInteger leadingZeroes(BigInteger argument) throws ExpressionArithmeticException {
        throw new ExpressionArithmeticException("«leadingZeroes» aren't supported by BigIntegers");
    }

    @Override
    public BigInteger trailingZeroes(BigInteger argument) throws ExpressionArithmeticException {
        throw new ExpressionArithmeticException("«trailingZeroes» aren't supported by BigIntegers");
    }

    @Override
    public BigInteger add(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return left.add(right);
    }

    @Override
    public BigInteger subtract(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return left.subtract(right);
    }

    @Override
    public BigInteger multiply(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return left.multiply(right);
    }

    @Override
    public BigInteger divide(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return ExpressionArithmeticException.wrapComputationWithException(() ->  left.divide(right));
    }

    @Override
    public BigInteger logicalShiftRight(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        throw new ExpressionArithmeticException("«logicalShiftRight» isn't supported by BigIntegers");
    }

    @Override
    public BigInteger logicalShiftLeft(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return left.shiftLeft(right.intValue());
    }

    @Override
    public BigInteger arithmeticShiftRight(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return left.shiftRight(right.intValue());
    }

    @Override
    public BigInteger max(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return left.max(right);
    }

    @Override
    public BigInteger min(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return left.min(right);
    }
}
