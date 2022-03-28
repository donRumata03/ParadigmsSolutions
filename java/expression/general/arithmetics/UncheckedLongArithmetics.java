package expression.general.arithmetics;

import expression.exceptions.CheckedIntMath;
import expression.general.exceptions.ExpressionArithmeticException;

public class UncheckedLongArithmetics implements ArithmeticEngine<Long> {

    @Override
    public Long fromInt(int value) {
        return (long)value;
    }

    @Override
    public Long parseSignedInt(String toParse) {
        return Long.parseLong(toParse);
    }

    @Override
    public Long abs(Long argument) {
        return Math.abs(argument);
    }

    @Override
    public Long negate(Long argument) {
        return -argument;
    }

    @Override
    public Long pow(Long left, Long right) throws ExpressionArithmeticException {
        return (long) CheckedIntMath.checkedPow(left.intValue(), right.intValue());
    }

    @Override
    public Long log(Long left, Long right) throws ExpressionArithmeticException {
        return (long) CheckedIntMath.checkedPow(left.intValue(), right.intValue());
    }

    @Override
    public Long leadingZeroes(Long argument) throws ExpressionArithmeticException {
        return (long) Long.numberOfLeadingZeros(argument);
    }

    @Override
    public Long trailingZeroes(Long argument) throws ExpressionArithmeticException {
        return (long) Long.numberOfTrailingZeros(argument);
    }

    @Override
    public Long count(Long argument) throws ExpressionArithmeticException {
        return (long) Long.bitCount(argument);
    }

    @Override
    public Long add(Long left, Long right) throws ExpressionArithmeticException {
        return left + right;
    }

    @Override
    public Long subtract(Long left, Long right) throws ExpressionArithmeticException {
        return left - right;
    }

    @Override
    public Long multiply(Long left, Long right) throws ExpressionArithmeticException {
        return left * right;
    }

    @Override
    public Long divide(Long left, Long right) throws ExpressionArithmeticException {
        return left / right;
    }

    @Override
    public Long logicalShiftRight(Long left, Long right) throws ExpressionArithmeticException {
        return left >>> right;
    }

    @Override
    public Long logicalShiftLeft(Long left, Long right) throws ExpressionArithmeticException {
        return left << right;
    }

    @Override
    public Long arithmeticShiftRight(Long left, Long right) throws ExpressionArithmeticException {
        return left >> right;
    }

    @Override
    public Long max(Long left, Long right) throws ExpressionArithmeticException {
        return Long.max(left, right);
    }

    @Override
    public Long min(Long left, Long right) throws ExpressionArithmeticException {
        return Long.min(left, right);
    }
}
