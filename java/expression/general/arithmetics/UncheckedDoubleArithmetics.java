package expression.general.arithmetics;

import expression.general.exceptions.ExpressionArithmeticException;

public class UncheckedDoubleArithmetics implements ArithmeticEngine<Double> {

    @Override
    public Double fromInt(int value) {
        return (double) value;
    }

    @Override
    public Double abs(Double argument) {
        return Math.abs(argument);
    }

    @Override
    public Double negate(Double argument) {
        return -argument;
    }

    @Override
    public Double pow(Double left, Double right) {
        return Math.pow(left, right);
    }

    @Override
    public Double log(Double left, Double right) {
        return Math.log(left) / Math.log(right);
    }

    @Override
    public Double leadingZeroes(Double argument) {
        throw new ExpressionArithmeticException("leadingZeroes isn't supported for doubles");
    }

    @Override
    public Double trailingZeroes(Double argument) {
        throw new ExpressionArithmeticException("trailingZeroes isn't supported for doubles");
    }

    @Override
    public Double count(Double argument) throws ExpressionArithmeticException {
        return (double) Long.bitCount(Double.doubleToLongBits(argument));
    }

    @Override
    public Double add(Double left, Double right) {
        return left + right;
    }

    @Override
    public Double subtract(Double left, Double right) {
        return left - right;
    }

    @Override
    public Double multiply(Double left, Double right) {
        return left * right;
    }

    @Override
    public Double divide(Double left, Double right) {
        return left / right;
    }

    @Override
    public Double logicalShiftRight(Double left, Double right) {
        throw new ExpressionArithmeticException("logicalShiftRight isn't supported for doubles");
    }

    @Override
    public Double logicalShiftLeft(Double left, Double right) {
        throw new ExpressionArithmeticException("logicalShiftLeft isn't supported for doubles");
    }

    @Override
    public Double arithmeticShiftRight(Double left, Double right) {
        throw new ExpressionArithmeticException("arithmeticShiftRight isn't supported for doubles");
    }

    @Override
    public Double max(Double left, Double right) {
        return Math.max(left, right);
    }

    @Override
    public Double min(Double left, Double right) {
        return Math.min(left, right);
    }
}
