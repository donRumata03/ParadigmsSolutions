package expression.exceptions;

import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.operations.LogicalShiftRight;

public class CheckedLogicalShiftRight
    extends LogicalShiftRight<Integer, CheckedIntegerArithmetics>
    implements IntOperationUnwrapper
{

    public CheckedLogicalShiftRight(ParenthesesTrackingExpression<Integer> left, ParenthesesTrackingExpression<Integer> right) {
        super(left, right, new CheckedIntegerArithmetics());
    }
}
