package expression.exceptions;

import expression.general.ParenthesesTrackingExpression;

public class CheckedNegate extends Negate {

    public CheckedNegate(ParenthesesTrackingExpression child) {
        super(child);
    }

    @Override
    public int reductionOperation(int childResult) {
        return CheckedIntMath.checkedNegate(childResult);
    }
}
