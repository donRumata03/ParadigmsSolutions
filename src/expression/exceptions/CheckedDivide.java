package expression.exceptions;

import expression.general.ParenthesesTrackingExpression;

public class CheckedDivide extends Divide {

    public CheckedDivide(ParenthesesTrackingExpression left, ParenthesesTrackingExpression right) {
        super(left, right);
    }

    @Override
    public int reductionOperation(int leftResult, int rightResult) {
        return CheckedIntMath.checkedDivide(leftResult, rightResult);
    }
}
