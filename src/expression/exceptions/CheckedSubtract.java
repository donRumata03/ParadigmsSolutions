package expression.exceptions;

import expression.Subtract;
import expression.general.ParenthesesTrackingExpression;

public class CheckedSubtract extends Subtract {

    public CheckedSubtract(ParenthesesTrackingExpression left,
        ParenthesesTrackingExpression right) {
        super(left, right);
    }

    @Override
    public int reductionOperation(int leftResult, int rightResult) {
       return CheckedIntMath.checkedSubtract(leftResult, rightResult);
    }
}
