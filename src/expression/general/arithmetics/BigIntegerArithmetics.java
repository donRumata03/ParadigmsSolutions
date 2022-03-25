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
        return null;
    }

    @Override
    public BigInteger negate(BigInteger argument) {
        return null;
    }

    @Override
    public BigInteger pow(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger log(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger leadingZeroes(BigInteger argument) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger trailingZeroes(BigInteger argument) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger add(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return left.add(right);
    }

    @Override
    public BigInteger subtract(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger multiply(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger divide(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger logicalShiftRight(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger logicalShiftLeft(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger arithmeticShiftRight(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger max(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public BigInteger min(BigInteger left, BigInteger right) throws ExpressionArithmeticException {
        return null;
    }
}
