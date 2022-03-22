package expression.parser.generic.parseInterpreters;

import expression.Add;
import expression.exceptions.CheckedAdd;
import expression.general.ParenthesesTrackingExpression;
import expression.parser.generic.tokens.AbstractOperationToken;

public class MaybeCheckedParseInterpreter extends TokenMatcher<Integer> {

    private boolean checked = false;

    public MaybeCheckedParseInterpreter(boolean checked) {
        this.checked = checked;
    }

    @Override
    public Integer parseSignedInt(String toParse) {
        return Integer.parseInt(toParse);
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
    ParenthesesTrackingExpression<Integer> constructUnaryMinus(ParenthesesTrackingExpression<Integer> child) {
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
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructMultiply(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructDivide(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructShiftLeft(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructLogicalShiftRight(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructArithmeticShiftRight(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructPow(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return null;
    }

    @Override
    ParenthesesTrackingExpression<Integer> constructLog(ParenthesesTrackingExpression<Integer> left,
        ParenthesesTrackingExpression<Integer> right) {
        return null;
    }
}
