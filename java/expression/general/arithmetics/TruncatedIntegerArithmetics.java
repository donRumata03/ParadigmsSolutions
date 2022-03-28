package expression.general.arithmetics;

import static expression.general.exceptions.ExpressionArithmeticException.wrapComputationWithException;

import expression.exceptions.CheckedIntMath;
import expression.general.exceptions.ExpressionArithmeticException;

public class TruncatedIntegerArithmetics implements ArithmeticEngine<Integer> {

    static int truncate10(int value) {
        return value / 10 * 10;
    }

    @Override
    public Integer fromInt(int value) {
        return truncate10(value);
    }

    @Override
    public Integer abs(Integer argument) {
        return truncate10(Math.abs(argument));
    }

    @Override
    public Integer negate(Integer argument) {
        return truncate10(-argument);
    }

    @Override
    public Integer pow(Integer left, Integer right) throws ExpressionArithmeticException {
        return truncate10(CheckedIntMath.checkedPow(left, right));
    }

    @Override
    public Integer log(Integer left, Integer right) throws ExpressionArithmeticException {
        return truncate10(CheckedIntMath.checkedLog(left, right));
    }

    @Override
    public Integer leadingZeroes(Integer argument) throws ExpressionArithmeticException {
        return truncate10(Integer.numberOfLeadingZeros(argument));
    }

    @Override
    public Integer trailingZeroes(Integer argument) throws ExpressionArithmeticException {
        return truncate10(Integer.numberOfTrailingZeros(argument));
    }

    @Override
    public Integer count(Integer argument) throws ExpressionArithmeticException {
        return truncate10(Integer.bitCount(argument));
    }

    @Override
    public Integer add(Integer left, Integer right) throws ExpressionArithmeticException {
        return truncate10(left + right);
    }

    @Override
    public Integer subtract(Integer left, Integer right) throws ExpressionArithmeticException {
        return truncate10(left - right);
    }

    @Override
    public Integer multiply(Integer left, Integer right) throws ExpressionArithmeticException {
        return truncate10(left * right);
    }

    @Override
    public Integer divide(Integer left, Integer right) throws ExpressionArithmeticException {
        return wrapComputationWithException(() -> truncate10(left / right));
    }

    @Override
    public Integer logicalShiftRight(Integer left, Integer right) throws ExpressionArithmeticException {
        return truncate10(left >>> right);
    }

    @Override
    public Integer logicalShiftLeft(Integer left, Integer right) throws ExpressionArithmeticException {
        return truncate10(left << right);
    }

    @Override
    public Integer arithmeticShiftRight(Integer left, Integer right) throws ExpressionArithmeticException {
        return truncate10(left >> right);
    }

    @Override
    public Integer max(Integer left, Integer right) throws ExpressionArithmeticException {
        return truncate10(Integer.max(left, right));
    }

    @Override
    public Integer min(Integer left, Integer right) throws ExpressionArithmeticException {
        return truncate10(Integer.min(left, right));
    }
}
