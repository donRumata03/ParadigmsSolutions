package expression.parser.generic.parseInterpreters;

import expression.Add;
import expression.Const;
import expression.Divide;
import expression.Multiply;
import expression.Subtract;
import expression.Variable;
import expression.exceptions.CheckedAdd;
import expression.exceptions.CheckedDivide;
import expression.exceptions.CheckedLog;
import expression.exceptions.CheckedMultiply;
import expression.exceptions.CheckedNegate;
import expression.exceptions.CheckedPow;
import expression.exceptions.CheckedSubtract;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.arithmetics.UncheckedIntegerArithmetics;
import expression.general.operations.ArithmeticShiftRight;
import expression.general.operations.Log;
import expression.general.operations.LogicalShiftRight;
import expression.general.operations.Negate;
import expression.general.operations.Pow;
import expression.general.operations.ShiftLeft;

/**
 * Depending on `checked` parameter generates checked or unchecked integer nodes
 */
public class MaybeCheckedParseInterpreter extends TokenMatcher<Integer> {

    private final boolean checked;

    public MaybeCheckedParseInterpreter(boolean checked) {
        this.checked = checked;
    }

    @Override
    public Integer parseSignedInt(String toParse) {
        return Integer.parseInt(toParse);
    }

    @Override
    public ParenthesesTrackingExpression<Integer> constructVariable(String name) {
        return new Variable(name);
    }

    @Override
    public ParenthesesTrackingExpression<Integer> constructConst(Integer value) {
        return new Const(value);
    }


    @Override
    ParenthesesTrackingExpression<Integer> constructLeadingZeroes(ParenthesesTrackingExpression<Integer> child) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructTrailingZeroes(ParenthesesTrackingExpression<Integer> child) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructAbs(ParenthesesTrackingExpression<Integer> child) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructCount(ParenthesesTrackingExpression<Integer> child) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructUnaryMinus(ParenthesesTrackingExpression<Integer> child) {
        return checked ? new CheckedNegate(child) : new Negate<>(child, new UncheckedIntegerArithmetics());
    }

    @Override
    protected ParenthesesTrackingExpression<Integer> constructMin(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return null;
    }

    @Override
    protected ParenthesesTrackingExpression<Integer> constructMax(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructAdd(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return checked ? new CheckedAdd(left, right) : new Add(left, right);
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructSubtract(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return checked ? new CheckedSubtract(left, right) : new Subtract(left, right);
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructMultiply(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return checked ? new CheckedMultiply(left, right) : new Multiply(left, right);
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructDivide(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return checked ? new CheckedDivide(left, right) : new Divide(left, right);
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructShiftLeft(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return new ShiftLeft<>(left, right, new CheckedIntegerArithmetics());
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructLogicalShiftRight(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return new LogicalShiftRight<>(left, right, new CheckedIntegerArithmetics());
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructArithmeticShiftRight(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return new ArithmeticShiftRight<>(left, right, new CheckedIntegerArithmetics());
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructPow(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return new CheckedPow(left, right);
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructLog(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return new CheckedLog(left, right);
    }
}
