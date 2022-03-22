package expression.exceptions;

import expression.general.ParenthesesTrackingExpression;

public class CheckedMultiply extends Multiply {

    public CheckedMultiply(ParenthesesTrackingExpression left,
        ParenthesesTrackingExpression right) {
        super(left, right);
    }

    @Override
    public int reductionOperation(int leftResult, int rightResult) {
        return CheckedIntMath.checkedMultiply(leftResult, rightResult);
    }
}
