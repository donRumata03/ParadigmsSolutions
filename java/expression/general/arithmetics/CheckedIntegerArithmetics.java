package expression.general.arithmetics;

import expression.exceptions.CheckedIntMath;
import expression.general.exceptions.ExpressionArithmeticException;
import expression.general.exceptions.IntegerArithmeticException;

public class CheckedIntegerArithmetics implements ArithmeticEngine<Integer> {

    @Override
    public Integer fromInt(int value) {
            return value;
        }

    @Override
    public Integer parseSignedInt(String toParse) {
        return Integer.parseInt(toParse);
    }

    @Override
    public Integer abs(Integer argument) {
        return CheckedIntMath.checkedAbs(argument);
    }

    @Override
    public Integer negate(Integer argument) {
        return CheckedIntMath.checkedNegate(argument);
    }

    @Override
    public Integer pow(Integer left, Integer right) throws ExpressionArithmeticException {
        return CheckedIntMath.checkedPow(left, right);
    }

    @Override
    public Integer log(Integer left, Integer right) throws ExpressionArithmeticException {
        return CheckedIntMath.checkedLog(left, right);
    }

    @Override
    public Integer leadingZeroes(Integer argument) throws ExpressionArithmeticException {
        return Integer.numberOfLeadingZeros(argument);
    }

    @Override
    public Integer trailingZeroes(Integer argument) throws ExpressionArithmeticException {
        return Integer.numberOfTrailingZeros(argument);
    }

    @Override
    public Integer count(Integer argument) throws ExpressionArithmeticException {
        return Integer.bitCount(argument);
    }

    @Override
    public Integer add(Integer left, Integer right) throws ExpressionArithmeticException {
        return CheckedIntMath.checkedAdd(left, right);
    }

    @Override
    public Integer subtract(Integer left, Integer right) throws ExpressionArithmeticException {
        return CheckedIntMath.checkedSubtract(left, right);
    }

    @Override
    public Integer multiply(Integer left, Integer right) throws ExpressionArithmeticException {
        return CheckedIntMath.checkedMultiply(left, right);
    }

    @Override
    public Integer divide(Integer left, Integer right) throws ExpressionArithmeticException {
        return CheckedIntMath.checkedDivide(left, right);
    }

    @Override
    public Integer logicalShiftRight(Integer left, Integer right) throws ExpressionArithmeticException {
        return left >>> right; // TODO: check right range?
    }

    @Override
    public Integer logicalShiftLeft(Integer left, Integer right) throws ExpressionArithmeticException {
        return left << right;
    }

    @Override
    public Integer arithmeticShiftRight(Integer left, Integer right) throws ExpressionArithmeticException {
        return left >> right;
    }


    @Override
    public Integer max(Integer left, Integer right) throws ExpressionArithmeticException {
        return Integer.max(left, right);
    }

    @Override
    public Integer min(Integer left, Integer right) throws ExpressionArithmeticException {
        return Integer.min(left, right);
    }
}
