package expression.general.arithmetics;

import expression.general.exceptions.ExpressionArithmeticException;

public class UncheckedDoubleArithmetics implements ArithmeticEngine<Double> {

    @Override
    public Double fromInt(int value) {
        return (double) value;
    }

    @Override
    public Double abs(Double argument) {
        return null;
    }

    @Override
    public Double negate(Double argument) {
        return null;
    }

    @Override
    public Double pow(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double log(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double leadingZeroes(Double argument) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double trailingZeroes(Double argument) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double add(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double subtract(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double multiply(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double divide(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double logicalShiftRight(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double logicalShiftLeft(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double arithmeticShiftRight(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double max(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Double min(Double left, Double right) throws ExpressionArithmeticException {
        return null;
    }
}
