package expression.general.arithmetics;

import expression.general.exceptions.ExpressionArithmeticException;
public class UncheckedIntegerArithmetics implements ArithmeticEngine<Integer> {

    @Override
    public Integer fromInt(int value) {
            return value;
        }

    @Override
    public Integer abs(Integer argument) {
        return argument > 0 ? argument : -argument;
    }

    @Override
    public Integer negate(Integer argument) {
        return -argument;
    }

    @Override
    public Integer pow(Integer left, Integer right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Integer log(Integer left, Integer right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Integer leadingZeroes(Integer argument) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Integer trailingZeroes(Integer argument) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Integer add(Integer left, Integer right) {
            return left + right;
        }

    @Override
    public Integer subtract(Integer left, Integer right) throws ExpressionArithmeticException {
        return left - right;
    }

    @Override
    public Integer multiply(Integer left, Integer right) throws ExpressionArithmeticException {
        return left * right;
    }

    @Override
    public Integer divide(Integer left, Integer right) throws ExpressionArithmeticException {
        return left / right;
    }

    @Override
    public Integer logicalShiftRight(Integer left, Integer right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Integer logicalShiftLeft(Integer left, Integer right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Integer arithmeticShiftRight(Integer left, Integer right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Integer max(Integer left, Integer right) throws ExpressionArithmeticException {
        return null;
    }

    @Override
    public Integer min(Integer left, Integer right) throws ExpressionArithmeticException {
        return null;
    }

    // …
    }
