package expression.exceptions;

import expression.general.IntOperationUnwrapper;
import expression.general.ParenthesesTrackingExpression;
import expression.general.arithmetics.CheckedIntegerArithmetics;
import expression.general.operations.ShiftLeft;

public class CheckedShiftLeft
    extends ShiftLeft<Integer, CheckedIntegerArithmetics>
    implements IntOperationUnwrapper
{

    public CheckedShiftLeft(ParenthesesTrackingExpression<Integer> left, ParenthesesTrackingExpression<Integer> right) {
        super(left, right, new CheckedIntegerArithmetics());
    }
}
